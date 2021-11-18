package by.vlfl.campos.presentation.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.R
import by.vlfl.campos.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment)
                as? SupportMapFragment
        mapFragment?.getMapAsync { map ->
            setupMarkerPosition(map)
        }
    }

    private fun setupMarkerPosition(map: GoogleMap) {

        val longitude = 27.57
        val latitude = 53.9

        val markerPosition = LatLng(latitude, longitude)

        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, MAP_ZOOM_LEVEL))
            addMarker(
                MarkerOptions()
                    .title("Minsk")
                    .position(markerPosition)
            )
        }
    }

    private companion object {
        private const val MAP_ZOOM_LEVEL = 15f
    }
}