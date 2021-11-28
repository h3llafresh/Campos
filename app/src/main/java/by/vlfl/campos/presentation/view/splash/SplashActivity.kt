package by.vlfl.campos.presentation.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.appComponent
import by.vlfl.campos.presentation.view.main.MainActivity
import by.vlfl.campos.presentation.view.signIn.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: SplashViewModel.Factory

    private val viewModel: SplashViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.appComponent.splashComponent().build().inject(this)

        observeViewModel()

        checkUserAuthorization()
    }

    private fun routeToSignInScreen() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun observeViewModel(){
        viewModel.navigateToMainActivityEvent.observe(this, { profileModel ->
            startActivity(MainActivity.create(this, profileModel))
        })
    }

    private fun checkUserAuthorization() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModel.provideUserLogin(currentUser.uid)
        } else {
            routeToSignInScreen()
        }
    }

}