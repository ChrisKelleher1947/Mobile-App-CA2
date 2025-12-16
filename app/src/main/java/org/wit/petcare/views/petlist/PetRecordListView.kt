package org.wit.petcare.views.petlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.petcare.R
import org.wit.petcare.activities.BaseActivity
import org.wit.petcare.adapters.PetCareListener
import org.wit.petcare.adapters.PetcareAdapter
import org.wit.petcare.databinding.ActivityPetRecordListBinding
import org.wit.petcare.models.PetCareModel

class PetRecordListView : BaseActivity(), PetCareListener {

    private lateinit var binding: ActivityPetRecordListBinding
    lateinit var presenter: PetRecordListPresenter

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        presenter = PetRecordListPresenter(this)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        presenter.loadNavHeader()        // Fix: Load user info
        setupDrawerNavigation()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        presenter.loadPets()             // Initial load

        binding.recyclerView.adapter = PetcareAdapter(emptyList(), this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadPets()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Search pets..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { presenter.filterPets(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { presenter.filterPets(it) }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return when (item.itemId) {
            R.id.item_add -> {
                presenter.doAddPet()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPetRecordClick(petrecord: PetCareModel) {
        presenter.openPetDetails(petrecord)
    }

    fun updateList(pets: List<PetCareModel>) {
        binding.recyclerView.adapter = PetcareAdapter(pets, this)
    }

    fun loadNavHeader(name: String, email: String) {
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<android.widget.TextView>(R.id.user_name).text = name
        headerView.findViewById<android.widget.TextView>(R.id.user_email).text = email
    }

    private fun setupDrawerNavigation() {
        binding.navView.setNavigationItemSelectedListener { item ->
            presenter.handleNavigation(item.itemId)
            binding.drawerLayout.closeDrawers()
            true
        }
    }
}
