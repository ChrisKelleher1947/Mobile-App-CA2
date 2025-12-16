package org.wit.petcare.views.map

import com.google.android.gms.maps.model.LatLng
import org.wit.petcare.models.Location

class MapPresenter(private val view: MapView) {

    private val location = Location(52.245696, -7.139102, 15f)

    init {
        view.intent.extras?.let {
            location.lat = it.getDouble("lat", location.lat)
            location.lng = it.getDouble("lng", location.lng)
            location.zoom = it.getFloat("zoom", location.zoom)
        }
    }

    fun doConfigureMap() {
        view.addMarker(
            LatLng(location.lat, location.lng),
            location.zoom
        )
    }

    fun doUpdateMarker(marker: com.google.android.gms.maps.model.Marker) {
        marker.snippet =
            "Lat: ${marker.position.latitude}, Lng: ${marker.position.longitude}"
        marker.showInfoWindow()
    }

    fun doUpdateLocation(position: LatLng, zoom: Float) {
        location.lat = position.latitude
        location.lng = position.longitude
        location.zoom = zoom
    }

    fun doSaveLocation() {
        view.returnLocation(location.lat, location.lng, location.zoom)
    }

}
