package org.wit.petcare.views.petdetails

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import org.wit.petcare.helpers.saveImageToInternalStorage

class PetDetailsPresenter(private val view: PetDetailsView) {

    private val app: MainApp = view.application as MainApp
    private lateinit var pet: PetCareModel

    fun loadPet() {
        pet = view.intent.getParcelableExtra("pet_record")!!
        view.showPet(pet)
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
        val intent = Intent(Intent.ACTION_PICK)
        view.startActivityForResult(intent, 0)
    }

    fun handleImageResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == RESULT_OK && result.data != null) {
            val uri = result.data!!.data!!
            pet.imagePath = saveImageToInternalStorage(view, uri)
            view.showPet(pet)
        }
    }

    fun doSetLocation() {
        val intent = Intent(view, org.wit.petcare.views.map.MapView::class.java)
            .putExtra("lat", pet.lat)
            .putExtra("lng", pet.lng)
            .putExtra("zoom", pet.zoom)
        view.startActivityForResult(intent, 1)
    }

    fun handleMapResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
            pet.lat = result.data!!.getDoubleExtra("lat", pet.lat)
            pet.lng = result.data!!.getDoubleExtra("lng", pet.lng)
            pet.zoom = result.data!!.getFloatExtra("zoom", pet.zoom)
            doConfigureMiniMap()
        }
    }

    fun doConfigureMiniMap() {
        view.updateMiniMap(pet.lat, pet.lng, pet.zoom)
    }
}
