package org.wit.petcare.views.home

import android.widget.TextView
import android.content.Intent
import org.wit.petcare.R
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel
import org.wit.petcare.views.addPet.PetCareView
import org.wit.petcare.views.auth.SignInView
import org.wit.petcare.views.petlist.PetRecordListView
import org.wit.petcare.views.settings.SettingsView

class HomePresenter(private val view: HomeView) {

    private val app: MainApp = view.application as MainApp

    fun init() {
        loadWelcomeText()
        loadQuickStats()
        loadRecentPets()
    }

    fun refreshData() {
        loadQuickStats()
        loadRecentPets()
    }

    fun doOpenPetList() {
        view.openActivity(Intent(view, PetRecordListView::class.java))
    }

    fun doAddPet() {
        view.openActivity(Intent(view, PetCareView::class.java))
    }

    fun doOpenPetDetails(pet: PetCareModel) {
        val intent = Intent(view, org.wit.petcare.views.petdetails.PetDetailsView::class.java)
        intent.putExtra("pet_record", pet)
        view.openActivity(intent)
    }

    fun handleNavMenu(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> {}
            R.id.nav_list -> doOpenPetList()
            R.id.nav_add_pet -> doAddPet()
            R.id.nav_settings -> view.openActivity(Intent(view, SettingsView::class.java))
            R.id.nav_logout -> view.logout()
        }
    }

    fun doSignOut() {
        view.openActivity(Intent(view, SignInView::class.java))
    }

    fun loadWelcomeText() {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        view.showWelcomeText("Welcome, ${user?.displayName ?: "User"}")
    }

    fun loadQuickStats() {
        app.petRecords.findAll { pets ->
            view.showQuickStats("You have ${pets.size} pets")
        }
    }

    fun loadRecentPets() {
        app.petRecords.findAll { pets ->
            view.showRecentPets(pets.takeLast(5))
        }
    }

    fun loadNavHeader(nameText: TextView, emailText: TextView) {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        emailText.text = user?.email ?: ""
        nameText.text = user?.displayName ?: user?.email?.substringBefore("@") ?: "User"
    }
}
