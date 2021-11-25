package by.vlfl.campos.presentation.view.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentProfileBinding
import by.vlfl.campos.presentation.view.signIn.SignInActivity
import com.firebase.ui.auth.AuthUI
import javax.inject.Inject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().appComponent.mainComponent()
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.logoutEvent.observe(viewLifecycleOwner, {
            AuthUI.getInstance().signOut(requireContext())
                .addOnCompleteListener {
                    startActivity(Intent(requireContext(), SignInActivity::class.java))
                    activity?.finish()
                }
        })
    }
}