package by.vlfl.campos.presentation.view.main.map

import androidx.lifecycle.ViewModel
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

class MapViewModel @Inject constructor(private val getPlaygroundsUseCase: GetPlaygroundsUseCase) : ViewModel() {

    private val _playgrounds = MutableSharedFlow<Playground>(replay = 1, onBufferOverflow = BufferOverflow.SUSPEND)
    val playgrounds: SharedFlow<Playground> = _playgrounds.asSharedFlow()

    init {
        viewModelScope.launch {
            _playgrounds.emitAll(getPlaygroundsUseCase())
        }
    }

}