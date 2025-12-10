package org.wit.petcare.activities

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.petcare.databinding.ActivityPetcareBinding
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import timber.log.Timber.i
import java.text.SimpleDateFormat
import java.util.*
import android.view.Menu
import android.view.MenuItem
import org.wit.petcare.R
import org.wit.petcare.adapters.PetcareAdapter
class PetCareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetcareBinding
    private var petRecord = PetCareModel()

    lateinit var app: MainApp
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPetcareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("PetCare Activity started..")

        //Date picker button
        binding.btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        //Save button
        binding.btnAddPet.setOnClickListener {
            val name = binding.petName.text.toString().trim()
            val type = binding.petType.text.toString().trim()
            val birthday = binding.tvSelectedDate.text.toString().replace("Selected Date: ", "").trim()

            // Check all fields
            if (name.isNotEmpty() && type.isNotEmpty() && birthday.isNotEmpty() && birthday != "No date selected") {
                petRecord.petName = name
                petRecord.petType = type
                petRecord.petBirthday = birthday
                app.petRecords.create(petRecord)

                i("Add Button Pressed: $petRecord")
                Snackbar.make(it, "Pet saved successfully!", Snackbar.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                val missingFields = mutableListOf<String>()
                if (name.isEmpty()) missingFields.add("name")
                if (type.isEmpty()) missingFields.add("type")
                if (birthday.isEmpty() || birthday == "No date selected") missingFields.add("birthday")

                val message = "Please enter: ${missingFields.joinToString(", ")}"
                Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
            }
        }

        binding.placemarkLocation.setOnClickListener {
            i ("Set Location Pressed")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_petcare, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.tvSelectedDate.text = getString(R.string.label_selected_date_prefix, formattedDate)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
