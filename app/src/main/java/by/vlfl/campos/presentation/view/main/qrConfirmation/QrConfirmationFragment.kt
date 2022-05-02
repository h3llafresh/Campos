package by.vlfl.campos.presentation.view.main.qrConfirmation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.vlfl.campos.R
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentQrConfirmationBinding
import by.vlfl.campos.utils.PermissionUtils
import com.budiyev.android.codescanner.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.zxing.BarcodeFormat
import javax.inject.Inject


class QrConfirmationFragment : Fragment() {

    private val args: QrConfirmationFragmentArgs by navArgs()

    private var _binding: FragmentQrConfirmationBinding? = null
    private val binding get() = _binding!!

    private var codeScanner: CodeScanner? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { permission ->
            if (!permission.value) {
                when (permission.key) {
                    Manifest.permission.CAMERA -> showMissingCameraPermissionDialog()
                    Manifest.permission.ACCESS_FINE_LOCATION -> showMissingLocationPermissionDialog()
                }
                findNavController().popBackStack()
            }
        }
    }

    @Inject
    lateinit var factory: QrConfirmationViewModel.Factory.AssistedInjectFactory

    private val viewModel: QrConfirmationViewModel by viewModels {
        factory.create(args.model)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.mainComponent().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        _binding = FragmentQrConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (hasCameraPermission() && hasLocationPermission()) initializeCodeScanner() else requestCameraAndLocationPermission()
        observeViewModelEvents()
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun initializeCodeScanner() {
        val scannerView = binding.vQrScanner
        codeScanner = CodeScanner(requireActivity(), scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    handleScanResult(it.text)
                }
            }
            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(), "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            scannerView.setOnClickListener {
                this.startPreview()
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraAndLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun showMissingCameraPermissionDialog() {
        PermissionUtils.DefaultPermissionDeniedDialog()
            .apply { message = this@QrConfirmationFragment.getString(R.string.qr_confirmation_camera_denied_dialog__permission_required_message) }
            .show(requireActivity().supportFragmentManager, QR_CONFIRMATION_CAMERA_PERMISSION_DENIED_DIALOG_TAG)
    }

    private fun showMissingLocationPermissionDialog() {
        PermissionUtils.DefaultPermissionDeniedDialog()
            .apply { message = this@QrConfirmationFragment.getString(R.string.qr_confirmation_location_denied_dialog__permission_required_message) }
            .show(requireActivity().supportFragmentManager, QR_CONFIRMATION_LOCATION_PERMISSION_DENIED_DIALOG_TAG)
    }

    @SuppressLint("MissingPermission")
    private fun handleScanResult(result: String) {
        if (hasLocationPermission()) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    val parsedResult = result.split(QR_SCAN_RESULT_DELIMITER)
                    binding.progressBar.isVisible = true
                    viewModel.checkUserIn(
                        parsedResult[0],
                        parsedResult[1],
                        currentLocation,
                        LatLng(parsedResult[2].toDouble(), parsedResult[3].toDouble())
                    )
                }
            }
        }
    }

    private fun observeViewModelEvents() = viewModel.confirmationResultEvent.observe(viewLifecycleOwner) { isConfirmed ->
        if (isConfirmed) {
            findNavController().navigate(QrConfirmationFragmentDirections.navigateToSuccessQrConfirmationFragment())
        } else {
            Toast.makeText(requireContext(), "Seems like you're too far from playground. Please try again...", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val QR_SCAN_RESULT_DELIMITER = "/"
        private const val QR_CONFIRMATION_LOCATION_PERMISSION_DENIED_DIALOG_TAG = "QrConfirmationPermissionDeniedDialog"
        private const val QR_CONFIRMATION_CAMERA_PERMISSION_DENIED_DIALOG_TAG = "QrConfirmationPermissionDeniedDialog"
    }
}
