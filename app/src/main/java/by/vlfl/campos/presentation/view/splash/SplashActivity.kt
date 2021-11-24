package by.vlfl.campos.presentation.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.presentation.view.main.MainActivity
import by.vlfl.campos.presentation.view.signIn.SignInActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            routeToMainScreen()
        } else {
            routeToSignInScreen()
        }
        finish()
    }

    private fun routeToMainScreen() = startActivity(Intent(this, MainActivity::class.java))

    private fun routeToSignInScreen() = startActivity(Intent(this, SignInActivity::class.java))

}