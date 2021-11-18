package by.vlfl.campos.presentation.view.signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.R
import by.vlfl.campos.databinding.ActivitySigninBinding
import by.vlfl.campos.presentation.view.main.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private var _binding: ActivitySigninBinding? = null
    private val binding get() = _binding!!

    private var _auth: FirebaseAuth? = null
    private val auth get() = _auth!!

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createSingInIntent()
    }

    private fun createSingInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_Campos)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Snackbar.make(binding.root, "OOOOPS! Something went wrong. Please sign in again", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {

            }
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {

            }
    }
}