package org.wit.petcare.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import org.wit.petcare.R
import org.wit.petcare.adapters.PetCareListener
import org.wit.petcare.adapters.PetcareAdapter
import org.wit.petcare.databinding.ActivityPetRecordListBinding
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel

class PetRecordListActivity : BaseActivity(), PetCareListener {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityPetRecordListBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                refreshList()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerNavigation()
        setupNavHeader()

        app = application as MainApp

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Load Firestore data
        refreshList()
    }

    private fun refreshList() {
        app.petRecords.findAll { pets ->
            binding.recyclerView.adapter = PetcareAdapter(pets, this)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search pets..."

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterPets(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterPets(it) }
                return true
            }
        })

        return true
    }

    private fun filterPets(query: String) {
        app.petRecords.findAll { pets ->
            val filtered = pets.filter {
                it.petName.contains(query, true) || it.petType.contains(query, true)
            }
            binding.recyclerView.adapter = PetcareAdapter(filtered, this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true

        when (item.itemId) {
            R.id.item_add -> getResult.launch(Intent(this, PetCareActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPetRecordClick(petrecord: PetCareModel) {
        startActivity(
            Intent(this, PetRecordDetailActivity::class.java)
                .putExtra("pet_record", petrecord)
        )
    }

    private fun setupDrawerNavigation() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.nav_list -> {} // Already here
                R.id.nav_add_pet -> startActivity(Intent(this, PetCareActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupNavHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val nameText = headerView.findViewById<TextView>(R.id.user_name)
        val emailText = headerView.findViewById<TextView>(R.id.user_email)
        val user = FirebaseAuth.getInstance().currentUser

        emailText.text = user?.email ?: ""
        nameText.text = user?.displayName ?: user?.email?.substringBefore("@") ?: "User"
    }
}

