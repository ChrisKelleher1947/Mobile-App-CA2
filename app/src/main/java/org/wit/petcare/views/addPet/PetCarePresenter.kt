package org.wit.petcare.views.addPet

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import org.wit.petcare.views.map.MapActivity
import timber.log.Timber

class PetCarePresenter(private val view: PetCareView) {

    private val pet = PetCareModel()
    private val app: MainApp = view.application as MainApp

    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>

    init {
        registerMapCallback()
    }

    fun doAddPet(name: String, type: String, birthday: String) {

        val missing = mutableListOf<String>()
        if (name.isEmpty()) missing.add("name")
        if (type.isEmpty()) missing.add("type")
        if (birthday.isEmpty()) missing.add("birthday")
        if (pet.lat == 0.0 && pet.lng == 0.0) missing.add("location")

        if (missing.isNotEmpty()) {
            view.showMessage("Please enter: ${missing.joinToString(", ")}")
            return
        }

        pet.petName = name
        pet.petType = type
        pet.petBirthday = birthday

        app.petRecords.create(pet) {
            view.setResult(AppCompatActivity.RESULT_OK)
            view.finish()
        }
    }

    fun doSetLocation() {
        val intent = Intent(view, MapActivity::class.java)
            .putExtra("lat", pet.lat)
            .putExtra("lng", pet.lng)
            .putExtra("zoom", pet.zoom)
        mapIntentLauncher.launch(intent)
    }

    fun cacheBirthday(date: String) {
        pet.petBirthday = date
        view.showDate(date)
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                    pet.lat = it.data!!.getDoubleExtra("lat", pet.lat)
                    pet.lng = it.data!!.getDoubleExtra("lng", pet.lng)
                    pet.zoom = it.data!!.getFloatExtra("zoom", pet.zoom)
                    Timber.i("Location set $pet")
                }
            }
    }
}
