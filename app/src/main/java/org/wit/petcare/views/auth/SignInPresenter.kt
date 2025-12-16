package org.wit.petcare.views.auth

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import org.wit.petcare.R

class SignInPresenter(private val view: SignInView) {

    private val auth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(view.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(view, gso)
    }

    fun doGoogleSignIn(launcher: ActivityResultLauncher<Intent>) {
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun handleGoogleSignIn(result: ActivityResult) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { firebaseAuthWithGoogle(it) }
        } catch (e: ApiException) {
            view.showMessage(e.message ?: "Google sign-in failed")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { view.goToHome() }
            .addOnFailureListener { view.showMessage(it.message ?: "Firebase auth failed") }
    }

    fun doEmailSignIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            view.showMessage("Email and password required")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { view.goToHome() }
            .addOnFailureListener { view.showMessage(it.message ?: "Authentication failed") }
    }

    fun doEmailSignUp(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            view.showMessage("Name, email and password required")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val profile = userProfileChangeRequest { displayName = name }
                auth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                    view.goToHome()
                }
            }
            .addOnFailureListener { view.showMessage(it.message ?: "Sign-up failed") }
    }

    fun checkUserSignedIn() {
        if (auth.currentUser != null) view.goToHome()
    }
}
