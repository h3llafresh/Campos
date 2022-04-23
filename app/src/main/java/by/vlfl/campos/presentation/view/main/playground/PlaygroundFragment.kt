package by.vlfl.campos.presentation.view.main.playground

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import by.vlfl.campos.R
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentPlaygroundBinding
import by.vlfl.campos.presentation.view.main.playground.adapter.ActivePlayersAdapter
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

        observeViewModelFlows()
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

    private fun setViewDataFromModel() = with(binding) {
        tvPlaygroundName.text = viewModel.model.name
        tvPlaygroundAddress.text = viewModel.model.address
        tvPlaygroundCategory.text = viewModel.model.category
    }

    private fun setEmptyPlaygroundViews(isPlaygroundEmpty: Boolean) = with(binding) {
        tvNoActivePlayers.isVisible = isPlaygroundEmpty
        tvCurrentlyThereLabel.isVisible = !isPlaygroundEmpty
        rvUsersOnPlayground.isVisible = !isPlaygroundEmpty
    }

    private fun observeViewModelFlows() {
        with(viewModel) {
            lifecycleScope.launchWhenStarted {
                activePlayers.collect { activePlayers ->
                    val filteredPlayers = activePlayers.filter { it.name != "" }
                    activePlayersAdapter?.replace(filteredPlayers)

                    setEmptyPlaygroundViews(filteredPlayers.isNullOrEmpty())
                }
            }
            lifecycleScope.launchWhenStarted {
                currentPlayground.collect { playground ->
                    with(binding) {
                        if (playground?.name.isNullOrEmpty()) {
                            bImIn.isVisible = true
                            tvYouAreOnPlayground.isVisible = false
                        } else {
                            bImIn.isVisible = false
                            tvYouAreOnPlayground.isVisible = true
                            if (playground!!.id == viewModel.model.id) {
                                tvYouAreOnPlayground.text = requireContext().getString(R.string.fragment_playground_you_are_on_this_playground)
                            } else {
                                tvYouAreOnPlayground.text =
                                    requireContext().getString(R.string.fragment_playground_you_are_on_another_playground, playground.name)
                            }
                        }
                    }
                }
            }
        }
    }
}