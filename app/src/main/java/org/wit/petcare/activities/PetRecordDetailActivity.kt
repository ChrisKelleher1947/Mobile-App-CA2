package org.wit.petcare.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.petcare.R
import org.wit.petcare.databinding.ActivityPetRecordDetailBinding
import org.wit.petcare.helpers.showImagePicker
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.petcare.helpers.saveImageToInternalStorage
import java.io.File


class PetRecordDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityPetRecordDetailBinding
    private lateinit var app: MainApp
    private lateinit var petRecord: PetCareModel
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var miniMap: MapView

    private lateinit var miniGoogleMap: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniMap = binding.minimap
        miniMap.onCreate(savedInstanceState)
        miniMap.getMapAsync { googleMap ->
            miniGoogleMap = googleMap
            updateMiniMap()
        }

        app = application as MainApp
        registerImagePickerCallback()
        registerMapCallback()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Pet Details"

        binding.hourPicker.minValue = 1
        binding.hourPicker.maxValue = 12

        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59

        binding.timePicker.minValue = 0
        binding.timePicker.maxValue = 1
        binding.timePicker.displayedValues = arrayOf("AM", "PM")

        intent.getParcelableExtra<PetCareModel>("pet_record")?.let { pet ->
            petRecord = pet
            binding.petNameDetail.text = pet.petName
            binding.petTypeDetail.text = pet.petType
            binding.petBirthdayDetail.text = pet.petBirthday
            binding.petNotes.setText(pet.notes)
            binding.hourPicker.value = pet.feedingHour
            binding.minutePicker.value = pet.feedingMinute
            binding.timePicker.value = if (pet.timePicker == "PM") 1 else 0
            if (pet.imagePath.isNotEmpty()) {
                Picasso.get()
                    .load(File(pet.imagePath))
                    .into(binding.placemarkImage)
            }

        }


        binding.btnSaveDetails.setOnClickListener {
            petRecord.notes = binding.petNotes.text.toString()
            petRecord.feedingHour = binding.hourPicker.value
            petRecord.feedingMinute = binding.minutePicker.value
            petRecord.timePicker = if (binding.timePicker.value == 0) "AM" else "PM"

            app.petRecords.update(petRecord) {
                Snackbar.make(it, "Saved successfully!", Snackbar.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(this, imageIntentLauncher)
        }

        binding.chooseLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("lat", petRecord.lat)
            intent.putExtra("lng", petRecord.lng)
            intent.putExtra("zoom", petRecord.zoom)
            mapIntentLauncher.launch(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pet_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val uri = result.data!!.data!!
                    val imagePath = saveImageToInternalStorage(this, uri)

                    petRecord.imagePath = imagePath

                    Picasso.get()
                        .load(File(imagePath))
                        .into(binding.placemarkImage)
                }
            }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_cancel -> {
                finish()
                true
            }

            R.id.item_delete -> {
                AlertDialog.Builder(this)
                    .setTitle("Delete Pet")
                    .setMessage("Are you sure you want to delete ${petRecord.petName}?")
                    .setPositiveButton("Delete") { _, _ ->
                        app.petRecords.delete(petRecord.id) {
                            Snackbar.make(binding.root, "Pet deleted", Snackbar.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            else -> super.onOptionsItemSelected(item)
        } as Boolean
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val data = result.data!!
                    petRecord.lat = data.getDoubleExtra("lat", petRecord.lat)
                    petRecord.lng = data.getDoubleExtra("lng", petRecord.lng)
                    petRecord.zoom = data.getFloatExtra("zoom", petRecord.zoom)
                    updateMiniMap()
                }
            }
    }
    private fun updateMiniMap() {
        if (::miniGoogleMap.isInitialized) {
            miniGoogleMap.clear()
            val loc = LatLng(petRecord.lat, petRecord.lng)
            miniGoogleMap.addMarker(MarkerOptions().position(loc).title("Pet Home"))
            miniGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, petRecord.zoom))
        }
    }
    override fun onResume() { super.onResume(); miniMap.onResume() }
    override fun onPause() { super.onPause(); miniMap.onPause() }
    override fun onDestroy() { super.onDestroy(); miniMap.onDestroy() }
    override fun onLowMemory() { super.onLowMemory(); miniMap.onLowMemory() }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        miniMap.onSaveInstanceState(outState)
    }


}


