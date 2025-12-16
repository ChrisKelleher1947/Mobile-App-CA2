package org.wit.petcare.DEPRECIATED
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.TextView
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.widget.Toolbar
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.card.MaterialCardView
//import com.google.android.material.navigation.NavigationView
//import com.google.firebase.auth.FirebaseAuth
//import org.wit.petcare.R
//import org.wit.petcare.activities.BaseActivity
//import org.wit.petcare.views.petlist.PetRecordListActivity
//import org.wit.petcare.views.settings.SettingsActivity
//import org.wit.petcare.views.auth.SignInActivity
//import org.wit.petcare.adapters.PetCareListener
//import org.wit.petcare.adapters.PetcareAdapter
//import org.wit.petcare.main.MainApp
//import org.wit.petcare.models.PetCareModel
//
//class HomeActivity : BaseActivity() {
//
//    private lateinit var toggle: ActionBarDrawerToggle
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var navView: NavigationView
//    private lateinit var welcomeText: TextView
//    private lateinit var quickStats: TextView
//    private lateinit var recentPetsRecycler: RecyclerView
//    lateinit var app: MainApp
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
//
//        drawerLayout = findViewById(R.id.drawerLayout)
//        navView = findViewById(R.id.navView)
//        welcomeText = findViewById(R.id.welcomeText)
//        quickStats = findViewById(R.id.quickStats)
//        recentPetsRecycler = findViewById(R.id.recentPetsRecycler)
//
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        setupNavHeader()
//        val cardPetList = findViewById<MaterialCardView>(R.id.cardPetList)
//        val cardAddPet = findViewById<MaterialCardView>(R.id.cardAddPet)
//
//        cardPetList.setOnClickListener {
//            startActivity(Intent(this, PetRecordListActivity::class.java))
//        }
//
//        cardAddPet.setOnClickListener {
//            startActivity(Intent(this, PetCareActivity::class.java))
//        }
//
//        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        navView.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.nav_home -> {}
//                R.id.nav_list -> startActivity(Intent(this, PetRecordListActivity::class.java))
//                R.id.nav_add_pet -> startActivity(Intent(this, PetCareActivity::class.java))
//                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
//                R.id.nav_logout -> {
//                    FirebaseAuth.getInstance().signOut()
//                    startActivity(Intent(this, SignInActivity::class.java))
//                    finish()
//                }
//            }
//            drawerLayout.closeDrawers()
//            true
//        }
//
//        app = application as MainApp
//
//        setupWelcomeText()
//        setupQuickStats()
//        setupRecentPets()
//    }
//
//    private fun setupNavHeader() {
//        val headerView = navView.getHeaderView(0)
//        val nameText = headerView.findViewById<TextView>(R.id.user_name)
//        val emailText = headerView.findViewById<TextView>(R.id.user_email)
//        val user = FirebaseAuth.getInstance().currentUser
//        emailText.text = user?.email ?: ""
//        nameText.text = user?.displayName ?: user?.email?.substringBefore("@") ?: "User"
//    }
//
//    private fun setupWelcomeText() {
//        val user = FirebaseAuth.getInstance().currentUser
//        welcomeText.text = "Welcome, ${user?.displayName ?: "User"}"
//    }
//
//    private fun setupQuickStats() {
//        app.petRecords.findAll { pets ->
//            val petCount = pets.size
//            quickStats.text = "You have $petCount pets"
//        }
//    }
//
//    private fun setupRecentPets() {
//        app.petRecords.findAll { recentPets ->
//            recentPetsRecycler.layoutManager =
//                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            recentPetsRecycler.adapter = PetcareAdapter(
//                recentPets.takeLast(5),
//                object : PetCareListener {
//                    override fun onPetRecordClick(petrecord: PetCareModel) {
//                        val intent = Intent(this@HomeActivity, PetRecordDetailActivity::class.java)
//                        intent.putExtra("pet_record", petrecord)
//                        startActivity(intent)
//                    }
//                },
//                true
//            )
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        setupQuickStats()
//        setupRecentPets()
//    }
//}