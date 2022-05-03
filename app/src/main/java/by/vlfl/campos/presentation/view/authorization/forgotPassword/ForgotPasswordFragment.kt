package by.vlfl.campos.presentation.view.authorization.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.R
import by.vlfl.campos.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding: FragmentForgotPasswordBinding get() = _binding!!

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOkButtonClickListener()
    }

    private fun setupOkButtonClickListener() = with(binding) {
        bOk.setOnClickListener {
            resetErrors()
            val recoveryEmail = etRecoveryEmail.text.toString()
            if (recoveryEmail.isNotEmpty()) {
                auth?.sendPasswordResetEmail(recoveryEmail)?.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(ForgotPasswordFragmentDirections.navigateToSuccessForgotPasswordFragment())
                    } else {
                        when (task.exception) {
                            is FirebaseAuthInvalidUserException -> tvNoEmailFound.visibility = View.VISIBLE
                            else -> tvUnknownError.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                tilRecoveryEmail.error = getString(R.string.fragment_forgot_password_text_enter_email)
            }
        }
    }

    private fun resetErrors() = with(binding) {
        tilRecoveryEmail.error = null
        tvUnknownError.visibility = View.GONE
        tvNoEmailFound.visibility = View.GONE
    }
}