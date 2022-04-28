package by.vlfl.campos.presentation.view.main.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.usecase.GetPlaygroundsUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.presentation.view.main.filter.ActivePlayersRange
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel(
    private val getPlaygroundsUseCase: GetPlaygroundsUseCase
) : ViewModel() {
    private var appliedActivePlayersFilters: List<ActivePlayersRange> = mutableListOf()
    private var appliedPlaygroundCategoryFilters: List<String> = mutableListOf()

    private val _playgrounds = MutableSharedFlow<Playground>()
    val playgrounds: SharedFlow<Playground> = _playgrounds

    private val _datasetUpdatedEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val datasetUpdatedEvent: LiveData<Nothing>
        get() = _datasetUpdatedEvent

    init {
        updateVm()
    }

    fun updateVm() = viewModelScope.launch {
        _playgrounds.emitAll(getPlaygroundsUseCase().filter { playground ->
            filterPlaygroundByActivePlayers(
                playground,
                appliedActivePlayersFilters
            ) && filterPlaygroundByCategory(
                playground,
                appliedPlaygroundCategoryFilters
            )
        })
    }

    fun applyPlaygroundFilters(activePlayersFilters: List<ActivePlayersRange>, sportCategoryFilters: List<String>) {
        appliedActivePlayersFilters = activePlayersFilters
        appliedPlaygroundCategoryFilters = sportCategoryFilters
    }

    private fun filterPlaygroundByActivePlayers(playground: Playground, activePlayersFilter: List<ActivePlayersRange>): Boolean {
        if (activePlayersFilter.isEmpty()) return true
        val activePlayersFilterResult = activePlayersFilter.fold(false) { accumulator, it ->
            accumulator || playground.activePlayers.count() in it.start..it.end
        }
        return activePlayersFilterResult
    }

    private fun filterPlaygroundByCategory(playground: Playground, playgroundCategoryFilter: List<String>): Boolean {
        if (playgroundCategoryFilter.isEmpty()) return true
        return playgroundCategoryFilter.contains(playground.category.name)
    }

    class Factory @Inject constructor(
        private val getPlaygroundsUseCase: GetPlaygroundsUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == MapViewModel::class.java)
            return MapViewModel(getPlaygroundsUseCase) as T
        }
    }
}