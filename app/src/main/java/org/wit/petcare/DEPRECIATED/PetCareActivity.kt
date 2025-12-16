package org.wit.petcare.DEPRECIATED
//
//import android.app.DatePickerDialog
//import android.content.Intent
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.MapView
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.material.snackbar.Snackbar
//import org.wit.petcare.R
//import org.wit.petcare.activities.BaseActivity
//import org.wit.petcare.databinding.ActivityPetcareBinding
//import org.wit.petcare.main.MainApp
//import org.wit.petcare.models.PetCareModel
//import org.wit.petcare.views.map.MapActivity
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//
//class PetCareActivity : BaseActivity() {
//
//    private lateinit var binding: ActivityPetcareBinding
//    private var petRecord = PetCareModel()
//
//    lateinit var app: MainApp
//    private val calendar = Calendar.getInstance()
//
//    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
//
//    private lateinit var miniMap: MapView
//    private lateinit var miniGoogleMap: GoogleMap
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        registerMapCallback()
//
//        binding = ActivityPetcareBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        miniMap = binding.minimap
//        miniMap.onCreate(savedInstanceState)
//        miniMap.getMapAsync { googleMap ->
//            miniGoogleMap = googleMap
//            updateMiniMap()
//        }
//
//        setSupportActionBar(binding.toolbar)
//        app = application as MainApp
//
//        binding.btnDatePicker.setOnClickListener { showDatePicker() }
//
//        binding.btnAddPet.setOnClickListener {
//            val name = binding.petName.text.toString().trim()
//            val type = binding.petType.text.toString().trim()
//            val birthday = binding.tvSelectedDate.text.toString().replace("Selected Date: ", "").trim()
//
//            val missingFields = mutableListOf<String>()
//
//            if (name.isEmpty()) missingFields.add("name")
//            if (type.isEmpty()) missingFields.add("type")
//            if (birthday.isEmpty() || birthday == "No date selected") missingFields.add("birthday")
//            if (petRecord.lat == 0.0 && petRecord.lng == 0.0) missingFields.add("location")
//
//            if (missingFields.isEmpty()) {
//                petRecord.petName = name
//                petRecord.petType = type
//                petRecord.petBirthday = birthday
//                app.petRecords.create(petRecord) {
//                    Snackbar.make(it, "Pet saved successfully!", Snackbar.LENGTH_SHORT).show()
//                    setResult(RESULT_OK)
//                    finish()
//                }
//            } else {
//                Snackbar.make(it, "Please enter: ${missingFields.joinToString(", ")}", Snackbar.LENGTH_LONG).show()
//            }
//        }
//
//        binding.chooseLocation.setOnClickListener {
//            val intent = Intent(this, MapActivity::class.java)
//            intent.putExtra("lat", petRecord.lat)
//            intent.putExtra("lng", petRecord.lng)
//            intent.putExtra("zoom", petRecord.zoom)
//            mapIntentLauncher.launch(intent)
//        }
//    }
//
//    private fun registerMapCallback() {
//        mapIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == RESULT_OK && result.data != null) {
//                    val data = result.data!!
//                    petRecord.lat = data.getDoubleExtra("lat", petRecord.lat)
//                    petRecord.lng = data.getDoubleExtra("lng", petRecord.lng)
//                    petRecord.zoom = data.getFloatExtra("zoom", petRecord.zoom)
//
//                    updateMiniMap()
//                }
//            }
//    }
//
//    private fun updateMiniMap() {
//        if (::miniGoogleMap.isInitialized) {
//            miniGoogleMap.clear()
//            val loc = LatLng(petRecord.lat, petRecord.lng)
//            miniGoogleMap.addMarker(MarkerOptions().position(loc).title("Pet Home"))
//            miniGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, petRecord.zoom))
//        }
//    }
//
//    private fun showDatePicker() {
//        val datePickerDialog = DatePickerDialog(
//            this,
//            { _, year, month, dayOfMonth ->
//                val selectedDate = Calendar.getInstance()
//                selectedDate.set(year, month, dayOfMonth)
//                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                val formattedDate = dateFormat.format(selectedDate.time)
//                binding.tvSelectedDate.text =
//                    getString(R.string.label_selected_date_prefix, formattedDate)
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//        datePickerDialog.show()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_petcare, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.item_cancel) finish()
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onResume() { super.onResume(); miniMap.onResume() }
//    override fun onPause() { super.onPause(); miniMap.onPause() }
//    override fun onDestroy() { super.onDestroy(); miniMap.onDestroy() }
//    override fun onLowMemory() { super.onLowMemory(); miniMap.onLowMemory() }
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        miniMap.onSaveInstanceState(outState)
//    }
//}