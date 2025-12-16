package org.wit.petcare.views.settings

import android.content.Context
import android.content.SharedPreferences
import org.wit.petcare.R
import androidx.core.content.edit

class SettingsPresenter(private val view: SettingsView) {

    private val prefs: SharedPreferences =
        view.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean("darkMode", false)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit { putBoolean("darkMode", enabled) }
        view.applyDarkMode(enabled)
    }

    fun handleNavigation(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> view.navigateToHome()
            R.id.nav_list -> view.navigateToPetList()
            R.id.nav_add_pet -> view.navigateToAddPet()
            R.id.nav_settings -> {}
            R.id.nav_logout -> view.signOut()
        }
    }
}
