package by.vlfl.campos.presentation.view.authorization.signIn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.vlfl.campos.databinding.ActivitySigninBinding

class SignInActivity : AppCompatActivity() {

    private var _binding: ActivitySigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}