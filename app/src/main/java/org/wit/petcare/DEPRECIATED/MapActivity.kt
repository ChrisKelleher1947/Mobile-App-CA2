package org.wit.petcare.DEPRECIATED//package org.wit.petcare.views.map
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.addCallback
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import org.wit.petcare.R
//import org.wit.petcare.databinding.ActivityMapBinding
//
//class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
//
//    private lateinit var map: GoogleMap
//    private lateinit var binding: ActivityMapBinding
//    private var locationLat = 52.245696
//    private var locationLng = -7.139102
//    private var locationZoom = 15f
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMapBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        // Get location from intent if passed
//        intent.extras?.let {
//            locationLat = it.getDouble("lat", locationLat)
//            locationLng = it.getDouble("lng", locationLng)
//            locationZoom = it.getFloat("zoom", locationZoom)
//        }
//
//        onBackPressedDispatcher.addCallback(this) {
//            val resultIntent = Intent()
//            resultIntent.putExtra("lat", locationLat)
//            resultIntent.putExtra("lng", locationLng)
//            resultIntent.putExtra("zoom", locationZoom)
//            setResult(RESULT_OK, resultIntent)
//            finish()
//        }
//        binding.btnSaveLocation.setOnClickListener {
//            val resultIntent = Intent()
//            resultIntent.putExtra("lat", locationLat)
//            resultIntent.putExtra("lng", locationLng)
//            resultIntent.putExtra("zoom", locationZoom)
//            setResult(RESULT_OK, resultIntent)
//            finish() // close map and return to PetCareActivity
//        }
//
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        val loc = LatLng(locationLat, locationLng)
//        val options = MarkerOptions()
//            .position(loc)
//            .title("Pet Home")
//            .snippet("GPS: $loc")
//            .draggable(true)
//        map.addMarker(options)
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, locationZoom))
//        map.setOnMarkerDragListener(this)
//    }
//
//    override fun onMarkerDrag(marker: Marker) {
//        marker.snippet = "Lat: ${marker.position.latitude}, Lng: ${marker.position.longitude}"
//        marker.showInfoWindow() // update popup immediately
//    }
//
//    override fun onMarkerDragEnd(marker: Marker) {
//        locationLat = marker.position.latitude
//        locationLng = marker.position.longitude
//        locationZoom = map.cameraPosition.zoom
//    }
//
//    override fun onMarkerDragStart(p0: Marker) {}
//}