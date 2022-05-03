package by.vlfl.campos.presentation.view.main.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentFilterBinding
import by.vlfl.campos.presentation.view.main.map.MapFragment
import com.google.android.material.chip.Chip
import javax.inject.Inject

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding
        get() = _binding!!

    @Inject
    lateinit var factory: FilterViewModel.Factory

    private val viewModel: FilterViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.mainComponent().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        setupApplyButtonClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChipCheckedChangeListeners()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupApplyButtonClickListener() = binding.bApply.setOnClickListener {
        setFragmentResult(
            MapFragment.FILTER_REQUEST_KEY,
            bundleOf(
                MapFragment.FILTER_REQUEST_BUNDLE_KEY to FilterResultModel(
                    activePlayersFilters = viewModel.playgroundActivePlayersFilters,
                    sportCategoryFilters = viewModel.playgroundCategoryFilters
                )
            )
        )
        findNavController().popBackStack()
    }

    private fun setupChipCheckedChangeListeners() {
        setupActivePlayersChipCheckedChangeListeners()
        setupPlaygroundCategoryChipCheckedChangeListeners()
    }

    private fun setupActivePlayersChipCheckedChangeListeners() = with(binding) {
        chipActivePlayersAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.clearPlaygroundActivePlayersFilter()
                cgActivePlayers.clearCheck()
                chipActivePlayersAll.isChecked = true
            }
        }

        setupActivePlayersRangeChipCheckedChangeListener(chipActivePlayersEmpty, ActivePlayersRange.EMPTY)
        setupActivePlayersRangeChipCheckedChangeListener(chipActivePlayers1To5, ActivePlayersRange(1, 5))
        setupActivePlayersRangeChipCheckedChangeListener(chipActivePlayers5To10, ActivePlayersRange(5, 10))
        setupActivePlayersRangeChipCheckedChangeListener(chipActivePlayers10To15, ActivePlayersRange(10, 15))
    }

    private fun setupActivePlayersRangeChipCheckedChangeListener(
        chip: Chip,
        activePlayersRange: ActivePlayersRange
    ) = chip.setOnCheckedChangeListener { _, isChecked ->
        handleActivePlayersRangeChipCheckedChange(activePlayersRange, isChecked)
    }

    private fun handleActivePlayersRangeChipCheckedChange(activePlayersRange: ActivePlayersRange, isChecked: Boolean) {
        if (isChecked) {
            viewModel.addPlaygroundActivePlayersFilter(activePlayersRange)
        } else {
            viewModel.removePlaygroundActivePlayersFilter(activePlayersRange)
        }
    }

    private fun setupPlaygroundCategoryChipCheckedChangeListeners() = with(binding) {
        chipSportCategoryAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.clearPlaygroundActivePlayersFilter()
                cgSportCategory.clearCheck()
                chipSportCategoryAll.isChecked = true
            }
        }

        setupPlaygroundCategoryNamedChipChangeListener(chipSportCategoryBasketball)
        setupPlaygroundCategoryNamedChipChangeListener(chipSportCategoryFootball)
        setupPlaygroundCategoryNamedChipChangeListener(chipSportCategoryVolleyball)
    }

    private fun setupPlaygroundCategoryNamedChipChangeListener(chip: Chip) = chip.setOnCheckedChangeListener { _, isChecked ->
        handlePlaygroundCategoryChipCheckedChange(chip.text.toString(), isChecked)
    }

    private fun handlePlaygroundCategoryChipCheckedChange(playgroundCategoryFilter: String, isChecked: Boolean) {
        if (isChecked) {
            viewModel.addPlaygroundCategoryFilter(playgroundCategoryFilter)
        } else {
            viewModel.removePlaygroundCategoryFilter(playgroundCategoryFilter)
        }
    }
}