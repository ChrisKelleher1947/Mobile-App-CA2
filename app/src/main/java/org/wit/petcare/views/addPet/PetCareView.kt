package org.wit.petcare.views.addPet

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.petcare.R
import org.wit.petcare.databinding.ActivityPetcareBinding
import java.text.SimpleDateFormat
import java.util.*

class PetCareView : AppCompatActivity() {

    private lateinit var binding: ActivityPetcareBinding
    lateinit var presenter: PetCarePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPetcareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = PetCarePresenter(this)

        binding.btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        binding.btnAddPet.setOnClickListener {
            presenter.doAddPet(
                binding.petName.text.toString(),
                binding.petType.text.toString(),
                binding.tvSelectedDate.text.toString()
                    .replace("Selected Date: ", "")
            )
        }

        binding.chooseLocation.setOnClickListener {
            presenter.doSetLocation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_petcare, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_cancel) finish()
        return super.onOptionsItemSelected(item)
    }

    fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    fun showDate(date: String) {
        binding.tvSelectedDate.text =
            getString(R.string.label_selected_date_prefix, date)
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, y, m, d ->
                val selected = Calendar.getInstance()
                selected.set(y, m, d)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                presenter.cacheBirthday(sdf.format(selected.time))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
