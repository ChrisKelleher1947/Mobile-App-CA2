package org.wit.petcare.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.wit.petcare.R
import org.wit.petcare.views.addPet.PetCareActivity
import org.wit.petcare.views.home.HomeActivity

class SettingsActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var switchDarkMode: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerNavigation()

        // Dark mode switch
        switchDarkMode = findViewById(R.id.switchDarkMode)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val nightMode = prefs.getBoolean("darkMode", false)

        switchDarkMode.isChecked = nightMode

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("darkMode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupDrawerNavigation() {
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    startActivity(Intent(this, HomeActivity::class.java))

                R.id.nav_list ->
                    startActivity(Intent(this, PetRecordListActivity::class.java))

                R.id.nav_add_pet ->
                    startActivity(Intent(this, PetCareActivity::class.java))

                R.id.nav_settings -> {
                    // already here
                }

                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}
