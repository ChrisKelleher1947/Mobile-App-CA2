package org.wit.petcare.views.petdetails

import android.app.Activity
import android.content.Intent
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import org.wit.petcare.helpers.saveImageToInternalStorage
import org.wit.petcare.helpers.showImagePicker

class PetDetailsPresenter(private val view: PetDetailsView) {

    private val app: MainApp = view.application as MainApp
    private lateinit var pet: PetCareModel

    fun loadPet() {
        pet = view.intent.getParcelableExtra("pet_record")!!
        view.initializePickers()
        view.showPet(pet)
        view.updateMiniMap(pet.location.lat, pet.location.lng, pet.location.zoom)
    }

    fun doSave(notes: String, hour: Int, minute: Int, ampm: String) {
        pet.notes = notes
        pet.feedingHour = hour
        pet.feedingMinute = minute
        pet.timePicker = ampm

        app.petRecords.update(pet) {
            view.showMessage("Saved successfully")
            view.finishWithResult()
        }
    }

    fun doDelete() {
        app.petRecords.delete(pet.id) {
            view.showMessage("Pet deleted")
            view.finishWithResult()
        }
    }

    fun doCancel() {
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(view, view.imageIntentLauncher)
    }

    fun handleImageResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = result.data!!.data!!
            pet.imagePath = saveImageToInternalStorage(view, uri)
            view.showPet(pet)
        }
    }

    fun doSetLocation() {
        val intent = Intent(view, org.wit.petcare.views.map.MapView::class.java)
            .putExtra("lat", pet.location.lat)
            .putExtra("lng", pet.location.lng)
            .putExtra("zoom", pet.location.zoom)
        view.launchMapIntent(intent)
    }

    fun handleMapResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            pet.location.lat = result.data!!.getDoubleExtra("lat", pet.location.lat)
            pet.location.lng = result.data!!.getDoubleExtra("lng", pet.location.lng)
            pet.location.zoom = result.data!!.getFloatExtra("zoom", pet.location.zoom)
            view.updateMiniMap(pet.location.lat, pet.location.lng, pet.location.zoom)
        }
    }
}
