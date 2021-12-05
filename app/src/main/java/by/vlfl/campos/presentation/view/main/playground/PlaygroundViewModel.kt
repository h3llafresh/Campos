package by.vlfl.campos.presentation.view.main.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.usecase.GetActivePlayersUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class PlaygroundViewModel(getActivePlayersUseCase: GetActivePlayersUseCase, val model: PlaygroundModel) : ViewModel() {

    private val _activePlayers = MutableSharedFlow<List<User>>(replay = CACHED_OBJECTS_NUMBER, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val activePlayers: SharedFlow<List<User>> = _activePlayers.asSharedFlow()

    init {
        viewModelScope.launch {
            _activePlayers.emitAll(getActivePlayersUseCase(playgroundId = model.id!!))
        }
    }

    class Factory @AssistedInject constructor(
        private val getActivePlayersUseCase: GetActivePlayersUseCase,
        @Assisted("playgroundModel") private val model: PlaygroundModel
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == PlaygroundViewModel::class.java)
            return PlaygroundViewModel(getActivePlayersUseCase, model) as T
        }

        @AssistedFactory
        interface AssistedInjectFactory {
            fun create(@Assisted("playgroundModel") model: PlaygroundModel): Factory
        }
    }

    companion object {
        private const val CACHED_OBJECTS_NUMBER = 10
    }

}