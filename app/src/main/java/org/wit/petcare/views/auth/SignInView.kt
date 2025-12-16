package org.wit.petcare.views.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.petcare.R
import org.wit.petcare.databinding.ActivitySignInBinding
import org.wit.petcare.views.home.HomeView

class SignInView : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    lateinit var presenter: SignInPresenter

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            presenter.handleGoogleSignIn(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_CA1ChrisKelleherMobileAppDev)
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = SignInPresenter(this)

        binding.Signin.setOnClickListener {
            presenter.doGoogleSignIn(googleSignInLauncher)
        }

        binding.emailSignInButton.setOnClickListener {
            presenter.doEmailSignIn(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

        binding.emailSignUpButton.setOnClickListener {
            presenter.doEmailSignUp(
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.checkUserSignedIn()
    }

    fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    fun goToHome() {
        startActivity(Intent(this, HomeView::class.java))
        finish()
    }
}
