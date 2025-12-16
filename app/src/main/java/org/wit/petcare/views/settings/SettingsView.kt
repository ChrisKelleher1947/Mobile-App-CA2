package org.wit.petcare.views.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.wit.petcare.R
import org.wit.petcare.views.auth.SignInView
import org.wit.petcare.views.addPet.PetCareView
import org.wit.petcare.views.home.HomeView
import org.wit.petcare.views.petlist.PetRecordListView

class SettingsView : AppCompatActivity() {

    lateinit var presenter: SettingsPresenter

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var switchDarkMode: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        presenter = SettingsPresenter(this)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerNavigation()
        setupDarkModeSwitch()
    }

    private fun setupDarkModeSwitch() {
        switchDarkMode = findViewById(R.id.switchDarkMode)
        switchDarkMode.isChecked = presenter.isDarkModeEnabled()
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            presenter.setDarkMode(isChecked)
        }
    }

    private fun setupDrawerNavigation() {
        navView.setNavigationItemSelectedListener { item ->
            presenter.handleNavigation(item.itemId)
            drawerLayout.closeDrawers()
            true
        }
    }

    fun applyDarkMode(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        switchDarkMode.isChecked = enabled
    }

    fun navigateToHome() = startActivity(Intent(this, HomeView::class.java))
    fun navigateToPetList() = startActivity(Intent(this, PetRecordListView::class.java))
    fun navigateToAddPet() = startActivity(Intent(this, PetCareView::class.java))
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, SignInView::class.java))
        finish()
    }
}
