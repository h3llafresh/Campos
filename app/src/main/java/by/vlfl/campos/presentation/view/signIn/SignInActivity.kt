package by.vlfl.campos.presentation.view.signIn

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.R
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.ActivitySigninBinding
import by.vlfl.campos.presentation.view.main.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {

    private var _binding: ActivitySigninBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: SignInViewModel.Factory

    private val viewModel: SignInViewModel by viewModels { factory }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.appComponent.signInComponent().build().inject(this)
        createSingInIntent()

        observeViewModel()
    }

    private fun createSingInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_AppTheme)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            viewModel.checkUserAuthorization()
        }
    }

    private fun observeViewModel() {
        viewModel.navigateToMainActivityEvent.observe(this, { profileModel ->
            startActivity(MainActivity.create(this, profileModel))
            finish()
        })
    }
}