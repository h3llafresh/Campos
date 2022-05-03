package by.vlfl.campos.presentation.view.authorization.signUp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.R
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    @Inject
    lateinit var factory: SignUpViewModel.Factory

    private val viewModel: SignUpViewModel by viewModels { factory }

    private var auth: FirebaseAuth? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.signInComponent().build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOnSignUpClickListener()
        observeViewModel()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupOnSignUpClickListener() = with(binding) {
        bSignUp.setOnClickListener {
            viewModel.validateInputs(
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString()
            )
        }
    }

    private fun observeViewModel() = with(viewModel) {
        invalidFirstNameEvent.observe(viewLifecycleOwner) {
            resetErrors()
            binding.tilFirstName.error = getString(R.string.fragment_sign_up_error_empty_first_name)
        }

        invalidLastNameEvent.observe(viewLifecycleOwner) {
            resetErrors()
            binding.tilLastName.error = getString(R.string.fragment_sign_up_error_empty_last_name)
        }

        invalidEmailEvent.observe(viewLifecycleOwner) {
            resetErrors()
            binding.tilEmail.error = getString(R.string.fragment_sign_up_error_invalid_email)
        }

        invalidPasswordEvent.observe(viewLifecycleOwner) {
            resetErrors()
            binding.tilPassword.error = getString(R.string.fragment_sign_up_error_invalid_password)
        }

        successValidationEvent.observe(viewLifecycleOwner) { credentials ->
            resetErrors()
            auth?.createUserWithEmailAndPassword(credentials.email, credentials.password)?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth?.currentUser ?: throw IllegalStateException("User wasn't retrieved")
                    user.updateProfile(
                        UserProfileChangeRequest.Builder().apply {
                            displayName = credentials.name
                        }.build()
                    )
                    viewModel.registerUserData(user.uid, credentials.name)
                } else {
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> binding.tvErrorEmailCollision.visibility = View.VISIBLE
                        else -> binding.tvErrorSignUp.visibility = View.VISIBLE
                    }
                }
            }
            findNavController().navigate(SignUpFragmentDirections.navigateToSignUpFragment())
        }
    }

    private fun resetErrors() = with(binding) {
        tilFirstName.error = null
        tilLastName.error = null
        tilEmail.error = null
        tilPassword.error = null
        binding.tvErrorSignUp.visibility = View.GONE
        binding.tvErrorEmailCollision.visibility = View.GONE
    }
}