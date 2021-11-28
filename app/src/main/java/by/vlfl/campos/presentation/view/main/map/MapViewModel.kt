package by.vlfl.campos.presentation.view.main.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.usecase.GetPlaygroundsUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel(getPlaygroundsUseCase: GetPlaygroundsUseCase) : ViewModel() {

    private val _playgrounds = MutableSharedFlow<Playground>(replay = 10, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val playgrounds: SharedFlow<Playground> = _playgrounds.asSharedFlow()

    init {
        viewModelScope.launch {
            _playgrounds.emitAll(getPlaygroundsUseCase())
        }
    }

    class Factory @Inject constructor(
        private val getPlaygroundsUseCase: GetPlaygroundsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == MapViewModel::class.java)
            Log.d("MapViewModel", "Creating VM")
            return MapViewModel(getPlaygroundsUseCase) as T
        }
    }

}