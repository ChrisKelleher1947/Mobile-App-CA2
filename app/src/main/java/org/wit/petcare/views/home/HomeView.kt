package org.wit.petcare.views.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.wit.petcare.R
import org.wit.petcare.helpers.BaseActivity
import org.wit.petcare.adapters.PetCareListener
import org.wit.petcare.adapters.PetcareAdapter
import org.wit.petcare.models.PetCareModel

class HomeView : BaseActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var welcomeText: TextView
    private lateinit var quickStats: TextView
    private lateinit var recentPetsRecycler: RecyclerView

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        welcomeText = findViewById(R.id.welcomeText)
        quickStats = findViewById(R.id.quickStats)
        recentPetsRecycler = findViewById(R.id.recentPetsRecycler)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        presenter = HomePresenter(this)
        presenter.init()  // Load data and setup UI

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupNavHeader()

        findViewById<MaterialCardView>(R.id.cardPetList).setOnClickListener {
            presenter.doOpenPetList()
        }

        findViewById<MaterialCardView>(R.id.cardAddPet).setOnClickListener {
            presenter.doAddPet()
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            presenter.handleNavMenu(menuItem.itemId)
            drawerLayout.closeDrawers()
            true
        }
    }

    fun showWelcomeText(text: String) {
        welcomeText.text = text
    }

    fun showQuickStats(text: String) {
        quickStats.text = text
    }

    fun showRecentPets(pets: List<PetCareModel>) {
        recentPetsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentPetsRecycler.adapter = PetcareAdapter(pets, object : PetCareListener {
            override fun onPetRecordClick(petrecord: PetCareModel) {
                presenter.doOpenPetDetails(petrecord)
            }
        }, true)
    }

    fun openActivity(intent: Intent) {
        startActivity(intent)
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        presenter.doSignOut()
        finish()
    }

    private fun setupNavHeader() {
        val headerView = navView.getHeaderView(0)
        val nameText = headerView.findViewById<TextView>(R.id.user_name)
        val emailText = headerView.findViewById<TextView>(R.id.user_email)
        presenter.loadNavHeader(nameText, emailText)
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshData()
    }
}
