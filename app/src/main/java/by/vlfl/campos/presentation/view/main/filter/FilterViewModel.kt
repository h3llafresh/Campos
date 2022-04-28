package by.vlfl.campos.presentation.view.main.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class FilterViewModel : ViewModel() {

    private val _playgroundCategoryFilters: MutableList<String> = mutableListOf()
    val playgroundCategoryFilters: List<String>
        get() = _playgroundCategoryFilters

    private val _playgroundActivePlayersFilters: MutableList<ActivePlayersRange> = mutableListOf()
    val playgroundActivePlayersFilters: List<ActivePlayersRange>
        get() = _playgroundActivePlayersFilters

    fun addPlaygroundCategoryFilter(newFilter: String) = _playgroundCategoryFilters.add(newFilter)

    fun removePlaygroundCategoryFilter(filterToRemove: String) = _playgroundCategoryFilters.remove(filterToRemove)

    fun addPlaygroundActivePlayersFilter(newFilter: ActivePlayersRange) = _playgroundActivePlayersFilters.add(newFilter)

    fun removePlaygroundActivePlayersFilter(filterToRemove: ActivePlayersRange) = _playgroundActivePlayersFilters.remove(filterToRemove)

    fun clearPlaygroundActivePlayersFilter() = _playgroundActivePlayersFilters.clear()

    class Factory @Inject constructor() : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == FilterViewModel::class.java)
            return FilterViewModel() as T
        }
    }
}