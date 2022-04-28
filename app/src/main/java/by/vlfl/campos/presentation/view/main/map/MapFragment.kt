package by.vlfl.campos.presentation.view.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.R
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentMapBinding
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.SportCategory
import by.vlfl.campos.presentation.view.main.filter.FilterResultModel
import by.vlfl.campos.presentation.view.main.playground.toUiModel
import by.vlfl.campos.utils.BitmapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var mapView: MapView? = null

    private var _googleMap: GoogleMap? = null
    private val googleMap: GoogleMap get() = _googleMap!!

    private var fusedLocationClient: FusedLocationProviderClient? = null

    @Inject
    lateinit var factory: MapViewModel.Factory

    private val viewModel: MapViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.mainComponent().build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(FILTER_REQUEST_KEY) { _, bundle ->
            val result = bundle.getParcelable<FilterResultModel>(FILTER_REQUEST_BUNDLE_KEY) ?: return@setFragmentResultListener
            viewModel.applyPlaygroundFilters(result.activePlayersFilters, result.sportCategoryFilters)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mapView = binding.gMapPlaygroundsMap

        setupFilterButtonClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVm()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)

        mapView?.onCreate(mapViewBundle)
        setupMap()
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateVm()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView?.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView?.onSaveInstanceState(mapViewBundle)
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun setupMap() {
        binding.gMapPlaygroundsMap.getMapAsync { map ->
            _googleMap = map
            with(googleMap) {
                setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.campos_map_style)
                )
                enableCurrentLocation()
                showDeviceLocation()
            }
            setOnMyLocationClickListener()

            observeViewModelFlowFields()
        }
    }

    private fun setOnMyLocationClickListener() = binding.bMyLocation.setOnClickListener {
        showAnimatedDeviceLocation()
    }

    private fun setupMarkerPosition(map: GoogleMap, playground: Playground) {
        val markerPosition = playground.coordinates.let { coordinates ->
            val latitude = coordinates.latitude ?: return
            val longitude = coordinates.longitude ?: return

            LatLng(latitude, longitude)
        }

        with(map) {
            addMarker(
                MarkerOptions()
                    .title(playground.name)
                    .position(markerPosition)
                    .icon(
                        BitmapHelper.vectorToBitmap(
                            requireContext(),
                            selectPlaygroundIcon(playground.category)
                        )
                    )
            ).also {
                if (it != null) {
                    it.tag = playground
                    setupMarkerClickListener()
                }
            }
        }
    }

    private fun selectPlaygroundIcon(playgroundCategory: SportCategory) = when (playgroundCategory) {
        SportCategory.Football -> R.drawable.ic_playground_football
        SportCategory.Basketball -> R.drawable.ic_playground_basketball
        SportCategory.Volleyball -> R.drawable.ic_playground_volleyball
    }

    private fun observeViewModelFlowFields() {
        lifecycleScope.launchWhenStarted {
            viewModel.playgrounds.collect { playground ->
                setupMarkerPosition(googleMap, playground)
            }
        }
    }

    private fun setupMarkerClickListener() {
        googleMap.setOnMarkerClickListener {
            findNavController().navigate(
                MapFragmentDirections.navigateToPlaygroundFragment(
                    (it.tag as Playground).toUiModel()
                )
            )
            true
        }
    }

    private fun setupFilterButtonClickListener() = binding.bFilter.setOnClickListener {
        findNavController().navigate(MapFragmentDirections.navigateToFilterFragment())
    }

    private fun checkLocationPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun observeVm() {
        viewModel.datasetUpdatedEvent.observe(viewLifecycleOwner) {
            googleMap.clear()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableCurrentLocation() {
        if (checkLocationPermissionGranted()) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun showDeviceLocation() {
        try {
            if (checkLocationPermissionGranted()) {
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    if (location != null) {
                        val locationCoordinates = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCoordinates, MAP_ZOOM_LEVEL))
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Location exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showAnimatedDeviceLocation() {
        try {
            if (checkLocationPermissionGranted()) {
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    if (location != null) {
                        val locationCoordinates = LatLng(location.latitude, location.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCoordinates, MAP_ZOOM_LEVEL))
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Location exception: %s", e.message, e)
        }
    }

    companion object {
        const val FILTER_REQUEST_KEY = "filterRequestKey"
        const val FILTER_REQUEST_BUNDLE_KEY = "filterRequestBundleKey"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val MAP_ZOOM_LEVEL = 15f
    }
}