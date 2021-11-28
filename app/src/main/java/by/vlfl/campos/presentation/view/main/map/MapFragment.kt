package by.vlfl.campos.presentation.view.main.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.NavGraphMainDirections
import by.vlfl.campos.R
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentMapBinding
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.presentation.view.main.playground.PlaygroundModel
import by.vlfl.campos.utils.BitmapHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var mapView: MapView? = null
    private var _googleMap: GoogleMap? = null
    private val googleMap: GoogleMap get() = _googleMap!!

    @Inject
    lateinit var factory: MapViewModel.Factory

    private val viewModel: MapViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.appComponent.mainComponent().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        mapView = binding.gMapPlaygroundsMap
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)

        mapView?.onCreate(mapViewBundle)
        setupMap()
    }

    override fun onStart() {
        super.onStart()
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
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.campos_map_style)
            )
            observeViewModel()
        }
    }

    private fun setupMarkerPosition(map: GoogleMap, playground: Playground) {

        val markerPosition = LatLng(playground.coordinates?.latitude!!, playground.coordinates?.longitude!!)

        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, MAP_ZOOM_LEVEL))
            addMarker(
                MarkerOptions()
                    .title(playground.name)
                    .position(markerPosition)
                    .icon(
                        BitmapHelper.vectorToBitmap(
                            requireContext(),
                            R.drawable.ic_playground
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

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.playgrounds
                .collect { playground ->
                    setupMarkerPosition(googleMap, playground)
                }
        }
    }

    private fun setupMarkerClickListener() {
        googleMap.setOnMarkerClickListener {
            findNavController()
                .navigate(
                    NavGraphMainDirections.navigateToPlaygroundFragment(
                        PlaygroundModel.fromDomainModel(it.tag as Playground)
                    )
                )
            true
        }
    }

    private companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val MAP_ZOOM_LEVEL = 15f
    }
}