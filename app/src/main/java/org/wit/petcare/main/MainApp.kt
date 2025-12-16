package org.wit.petcare.main

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.wit.petcare.main.repository.PetCareFirestore
import org.wit.petcare.main.repository.PetCareStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var petRecords: PetCareStore

    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("PetCare started")

        auth = Firebase.auth
        petRecords  = PetCareFirestore()
    }
}
