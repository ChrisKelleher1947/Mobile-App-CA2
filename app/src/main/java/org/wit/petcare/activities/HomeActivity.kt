package org.wit.petcare.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.wit.petcare.R

class HomeActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupNavHeader()


        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                // Already here
                }

                R.id.nav_list -> {
                    startActivity(Intent(this, PetRecordListActivity::class.java))
                }

                R.id.nav_add_pet -> {
                    startActivity(Intent(this, PetCareActivity::class.java))
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

    private fun setupNavHeader() {
        val headerView = navView.getHeaderView(0)

        val nameText = headerView.findViewById<TextView>(R.id.user_name)
        val emailText = headerView.findViewById<TextView>(R.id.user_email)

        val user = FirebaseAuth.getInstance().currentUser

        emailText.text = user?.email ?: ""

        nameText.text =
            user?.displayName
                ?: user?.email?.substringBefore("@")
                        ?: "User"
    }

}
