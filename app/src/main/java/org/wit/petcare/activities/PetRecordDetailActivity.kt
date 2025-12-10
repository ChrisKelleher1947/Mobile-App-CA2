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
import timber.log.Timber.i

class PetRecordDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetRecordDetailBinding
    private lateinit var app: MainApp
    private lateinit var petRecord: PetCareModel

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        registerImagePickerCallback()
        registerMapCallback()

        setSupportActionBar(binding.toolbarDetail)
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
            if (pet.imageUri.isNotEmpty()) {
                Picasso.get()
                    .load(pet.imageUri)
                    .into(binding.placemarkImage)
            }

        }


        binding.btnSaveDetails.setOnClickListener {
            petRecord.notes = binding.petNotes.text.toString()
            petRecord.feedingHour = binding.hourPicker.value
            petRecord.feedingMinute = binding.minutePicker.value
            petRecord.timePicker = if (binding.timePicker.value == 0) "AM" else "PM"
            app.petRecords.update(petRecord)
            Snackbar.make(it, "Saved successfully!", Snackbar.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
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
                    petRecord.imageUri = uri.toString()
                    Picasso.get()
                        .load(uri)
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
                        app.petRecords.delete(petRecord)
                        Snackbar.make(binding.root, "Pet deleted", Snackbar.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val data = result.data!!
                    petRecord.lat = data.getDoubleExtra("lat", petRecord.lat)
                    petRecord.lng = data.getDoubleExtra("lng", petRecord.lng)
                    petRecord.zoom = data.getFloatExtra("zoom", petRecord.zoom)
                }
            }
    }

}


