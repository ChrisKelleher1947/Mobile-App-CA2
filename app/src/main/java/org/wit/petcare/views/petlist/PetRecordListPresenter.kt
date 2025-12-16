package org.wit.petcare.views.petlist

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import org.wit.petcare.R
import org.wit.petcare.main.MainApp
import org.wit.petcare.models.PetCareModel

class PetRecordListPresenter(private val view: PetRecordListView) {

    private val app: MainApp = view.application as MainApp

    fun loadPets() {
        app.petRecords.findAll { pets ->
            view.updateList(pets)
        }
    }

    fun filterPets(query: String) {
        app.petRecords.findAll { pets ->
            val filtered = pets.filter {
                it.petName.contains(query, true) || it.petType.contains(query, true)
            }
            view.updateList(filtered)
        }
    }

    fun doAddPet() {
        view.startActivity(Intent(view, org.wit.petcare.views.addPet.PetCareView::class.java))
    }

    fun openPetDetails(pet: PetCareModel) {
        val intent = Intent(view, org.wit.petcare.views.petdetails.PetDetailsView::class.java)
        intent.putExtra("pet_record", pet)
        view.startActivity(intent)
    }

    fun loadNavHeader() {
        val user = FirebaseAuth.getInstance().currentUser
        val name = user?.displayName ?: user?.email?.substringBefore("@") ?: "User"
        val email = user?.email ?: ""
        view.loadNavHeader(name, email)
    }

    fun handleNavigation(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> view.startActivity(Intent(view, org.wit.petcare.views.home.HomeView::class.java))
            R.id.nav_list -> { } // already in PetRecordList
            R.id.nav_add_pet -> view.startActivity(Intent(view, org.wit.petcare.views.addPet.PetCareView::class.java))
            R.id.nav_settings -> view.startActivity(Intent(view, org.wit.petcare.views.settings.SettingsView::class.java))
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                view.startActivity(Intent(view, org.wit.petcare.views.auth.SignInView::class.java))
                view.finish()
            }
        }
    }
}

