package org.wit.petcare.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import org.wit.petcare.R
import org.wit.petcare.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Snackbar.make(binding.root, e.message ?: "Google sign-in failed", Snackbar.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.Signin.setOnClickListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }

        binding.emailSignInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Snackbar.make(binding.root, "Email and password required", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { goToHome() }
                .addOnFailureListener {
                    Snackbar.make(binding.root, it.message ?: "Authentication failed", Snackbar.LENGTH_LONG).show()
                }
        }

        binding.emailSignUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Snackbar.make(binding.root, "Name, email and password required", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val profile = userProfileChangeRequest {
                        displayName = name
                    }
                    auth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                        goToHome()
                    }
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root, it.message ?: "Sign-up failed", Snackbar.LENGTH_LONG).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) goToHome()
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { goToHome() }
            .addOnFailureListener {
                Snackbar.make(binding.root, it.message ?: "Firebase authentication failed", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
