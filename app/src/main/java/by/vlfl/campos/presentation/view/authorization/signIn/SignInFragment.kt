package by.vlfl.campos.presentation.view.authorization.signIn

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentSignInBinding
import by.vlfl.campos.presentation.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: SignInViewModel.Factory

    private val viewModel: SignInViewModel by viewModels { factory }

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
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnSignInClickListener()
        setOnSignUpClickListener()
        setupOnForgotPasswordClickListener()
        observeViewModel()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupOnForgotPasswordClickListener() = binding.tvForgotPassword.setOnClickListener {
        findNavController().navigate(SignInFragmentDirections.navigateToForgotPasswordFragment())
    }

    private fun setOnSignInClickListener() = with(binding) {
        bSignIn.setOnClickListener {
            val enteredEmail = etEmailInput.text.toString().also {
                if (it.isEmpty()) return@setOnClickListener
            }
            val enteredPassword = etPasswordInput.text.toString().also {
                if (it.isEmpty()) return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            auth?.signInWithEmailAndPassword(
                enteredEmail,
                enteredPassword
            )?.addOnCompleteListener(requireActivity()) { authResult ->
                if (authResult.isSuccessful) {
                    viewModel.checkUserAuthorization()
                } else {
                    progressBar.visibility = View.GONE
                    tvUserNotFoundError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setOnSignUpClickListener() = binding.bSignUp.setOnClickListener {
        findNavController().navigate(SignInFragmentDirections.navigateToSignUpFragment())
    }

    private fun observeViewModel() {
        viewModel.navigateToMainActivityEvent.observe(viewLifecycleOwner) { profileModel ->
            startActivity(MainActivity.create(requireContext(), profileModel))
            requireActivity().finish()
        }
    }
}