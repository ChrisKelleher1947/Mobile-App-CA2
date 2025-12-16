package org.wit.petcare.views.addPet

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import org.wit.petcare.views.map.MapView
import timber.log.Timber
import android.app.Activity


class PetCarePresenter(private val view: PetCareView) {

    val pet = PetCareModel()
    private val app: MainApp = view.application as MainApp

    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private var miniGoogleMap: GoogleMap? = null

    init {
        registerMapCallback()
    }

    fun attachMap(googleMap: GoogleMap) {
        miniGoogleMap = googleMap
        updateMiniMap()
    }

    fun doAddPet(name: String, type: String, birthday: String) {
        val missing = mutableListOf<String>()
        if (name.isEmpty()) missing.add("name")
        if (type.isEmpty()) missing.add("type")
        if (birthday.isEmpty()) missing.add("birthday")
        if (pet.location.lat == 0.0 && pet.location.lng == 0.0) missing.add("location")

        if (missing.isNotEmpty()) {
            view.showMessage("Please enter: ${missing.joinToString(", ")}")
            return
        }

        pet.petName = name
        pet.petType = type
        pet.petBirthday = birthday

        app.petRecords.create(pet) {
            view.setResult(Activity.RESULT_OK)
            view.finish()
        }
    }

    fun doSetLocation() {
        val intent = Intent(view, MapView::class.java)
            .putExtra("lat", pet.location.lat)
            .putExtra("lng", pet.location.lng)
            .putExtra("zoom", pet.location.zoom)
        mapIntentLauncher.launch(intent)
    }

    fun cacheBirthday(date: String) {
        pet.petBirthday = date
        view.showDate(date)
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                    pet.location.lat = it.data!!.getDoubleExtra("lat", pet.location.lat)
                    pet.location.lng = it.data!!.getDoubleExtra("lng", pet.location.lng)
                    pet.location.zoom = it.data!!.getFloatExtra("zoom", pet.location.zoom)
                    Timber.i("Location set $pet")
                    updateMiniMap()
                }
            }
    }

    private fun updateMiniMap() {
        miniGoogleMap?.let { map ->
            val loc = LatLng(pet.location.lat, pet.location.lng)
            map.clear()
            map.addMarker(MarkerOptions().position(loc).title("Pet Home"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, pet.location.zoom))
        }
    }
}
