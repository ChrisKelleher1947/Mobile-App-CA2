package org.wit.petcare.views.map

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.wit.petcare.R
import org.wit.petcare.databinding.ActivityMapBinding

class MapView :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener {

    private lateinit var binding: ActivityMapBinding
    lateinit var presenter: MapPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MapPresenter(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map)
                    as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnSaveLocation.setOnClickListener {
            presenter.doSaveLocation()
        }

        onBackPressedDispatcher.addCallback(this) {
            presenter.doSaveLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.doConfigureMap()
    }

    override fun onMarkerDrag(marker: Marker) {
        presenter.doUpdateMarker(marker)
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position, map.cameraPosition.zoom)
    }

    override fun onMarkerDragStart(marker: Marker) {}

    fun addMarker(position: LatLng, zoom: Float) {
        val options = MarkerOptions()
            .position(position)
            .title("Pet Home")
            .snippet("GPS: $position")
            .draggable(true)

        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
        map.setOnMarkerDragListener(this)
    }

    fun returnLocation(lat: Double, lng: Double, zoom: Float) {
        val result = Intent()
        result.putExtra("lat", lat)
        result.putExtra("lng", lng)
        result.putExtra("zoom", zoom)
        setResult(RESULT_OK, result)
        finish()
    }
}
