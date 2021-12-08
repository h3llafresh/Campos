package by.vlfl.campos.presentation.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.appComponent
import by.vlfl.campos.presentation.view.main.MainActivity
import by.vlfl.campos.presentation.view.signIn.SignInActivity
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

        viewModel.checkUserAuthorization()
    }

    private fun observeViewModel() {
        with (viewModel) {
            navigateToMainActivityEvent.observe(this@SplashActivity, { profileModel ->
                startActivity(MainActivity.create(this@SplashActivity, profileModel))
                finish()
            })

            navigateToSignInEvent.observe(this@SplashActivity, {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            })
        }

    }
}