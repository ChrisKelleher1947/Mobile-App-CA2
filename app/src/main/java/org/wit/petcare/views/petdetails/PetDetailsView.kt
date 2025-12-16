package org.wit.petcare.views.petdetails

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.petcare.R
import org.wit.petcare.activities.BaseActivity
import org.wit.petcare.databinding.ActivityPetRecordDetailBinding
import org.wit.petcare.models.PetCareModel
import java.io.File

class PetDetailsView : BaseActivity() {

    private lateinit var binding: ActivityPetRecordDetailBinding
    private lateinit var presenter: PetDetailsPresenter

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>

    private lateinit var miniMap: MapView
    private lateinit var miniGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = PetDetailsPresenter(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Pet Details"

        miniMap = binding.minimap
        miniMap.onCreate(savedInstanceState)
        miniMap.getMapAsync {
            miniGoogleMap = it
            presenter.doConfigureMiniMap()
        }

        registerImagePickerCallback()
        registerMapCallback()

        presenter.loadPet()

        binding.btnSaveDetails.setOnClickListener {
            presenter.doSave(
                binding.petNotes.text.toString(),
                binding.hourPicker.value,
                binding.minutePicker.value,
                if (binding.timePicker.value == 0) "AM" else "PM"
            )
        }

        binding.chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        binding.chooseLocation.setOnClickListener {
            presenter.doSetLocation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pet_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
                true
            }
            R.id.item_delete -> {
                confirmDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    fun showPet(pet: PetCareModel) {
        binding.petNameDetail.text = pet.petName
        binding.petTypeDetail.text = pet.petType
        binding.petBirthdayDetail.text = pet.petBirthday
        binding.petNotes.setText(pet.notes)
        binding.hourPicker.value = pet.feedingHour
        binding.minutePicker.value = pet.feedingMinute
        binding.timePicker.value = if (pet.timePicker == "PM") 1 else 0

        if (pet.imagePath.isNotEmpty()) {
            Picasso.get().load(File(pet.imagePath)).into(binding.placemarkImage)
        }
    }

    fun updateMiniMap(lat: Double, lng: Double, zoom: Float) {
        miniGoogleMap.clear()
        val loc = LatLng(lat, lng)
        miniGoogleMap.addMarker(MarkerOptions().position(loc).title("Pet Home"))
        miniGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom))
    }

    fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    fun finishWithResult() {
        setResult(RESULT_OK)
        finish()
    }


    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Pet")
            .setMessage("Are you sure?")
            .setPositiveButton("Delete") { _, _ ->
                presenter.doDelete()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                presenter.handleImageResult(it)
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                presenter.handleMapResult(it)
            }
    }

    override fun onResume() { super.onResume(); miniMap.onResume() }
    override fun onPause() { super.onPause(); miniMap.onPause() }
    override fun onDestroy() { super.onDestroy(); miniMap.onDestroy() }
    override fun onLowMemory() { super.onLowMemory(); miniMap.onLowMemory() }
}
