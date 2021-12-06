package by.vlfl.campos.presentation.view.main.playground

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentPlaygroundBinding
import by.vlfl.campos.presentation.view.main.playground.adapter.ActivePlayersAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class PlaygroundFragment : Fragment() {

    private val args: PlaygroundFragmentArgs by navArgs()

    private var _binding: FragmentPlaygroundBinding? = null
    private val binding get() = _binding!!

    private var activePlayersAdapter: ActivePlayersAdapter? = null

    @Inject
    lateinit var factory: PlaygroundViewModel.Factory.AssistedInjectFactory

    private val viewModel: PlaygroundViewModel by viewModels {
        factory.create(args.model)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.mainComponent().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setViewDataFromModel()
        setupActivePlayersRecyclerView()

        observeViewModel()
    }

    private fun setupActivePlayersRecyclerView() {
        activePlayersAdapter = ActivePlayersAdapter()
        with(binding.rvUsersOnPlayground) {
            adapter = activePlayersAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setViewDataFromModel() {
        with(binding) {
            tvPlaygroundName.text = viewModel.model.name
            tvPlaygroundAddress.text = viewModel.model.address
            tvPlaygroundCategory.text = viewModel.model.category
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.activePlayers.collect { activePlayers ->
                activePlayersAdapter?.replace(activePlayers)
            }
        }
    }
}