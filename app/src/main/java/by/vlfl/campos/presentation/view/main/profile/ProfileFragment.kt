package by.vlfl.campos.presentation.view.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentProfileBinding
import by.vlfl.campos.domain.entity.UserCurrentPlayground
import by.vlfl.campos.presentation.view.authorization.SignInActivity
import com.firebase.ui.auth.AuthUI
import javax.inject.Inject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val args: ProfileFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ProfileViewModel.Factory

    private val viewModel: ProfileViewModel by viewModels { factory }

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

        setViewDataFromModel(args.model)
        observeViewModelEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModelEvents() {
        viewModel.logoutEvent.observe(viewLifecycleOwner) {
            AuthUI.getInstance().signOut(requireContext())
                .addOnCompleteListener {
                    startActivity(Intent(requireContext(), SignInActivity::class.java))
                    activity?.finish()
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currentPlayground
                .collect { playground ->
                    if (playground != null && playground.playgroundName.isNotEmpty()) {
                        setCurrentPlayground(playground)
                        binding.bLeaveGame.isVisible = true
                    } else {
                        clearCurrentPlayground()
                        binding.bLeaveGame.isVisible = false
                    }
                }
        }
    }

    private fun setCurrentPlayground(playground: UserCurrentPlayground) = with(binding) {
        if (!tvCurrentPlaygroundTitle.isVisible) {
            tvCurrentPlaygroundTitle.isVisible = true
            tvCurrentPlayground.isVisible = true
        }
        tvNoActiveGames.isVisible = false
        tvCurrentPlayground.text = playground.playgroundName
    }

    private fun clearCurrentPlayground() = with(binding) {
        if (tvCurrentPlaygroundTitle.isVisible) {
            tvCurrentPlaygroundTitle.isVisible = false
            tvCurrentPlayground.isVisible = false
            tvNoActiveGames.isVisible = true
        }
    }

    private fun setViewDataFromModel(model: ProfileUiModel) = with(binding) {
        tvUserName.text = model.name
        tvUserBirthDate.text = model.birthDate
    }
}