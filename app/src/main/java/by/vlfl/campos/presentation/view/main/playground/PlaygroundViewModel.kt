package by.vlfl.campos.presentation.view.main.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.entity.User
import by.vlfl.campos.domain.entity.UserCurrentPlayground
import by.vlfl.campos.domain.usecase.CheckInCurrentUserUseCase
import by.vlfl.campos.domain.usecase.GetActivePlayersUseCase
import by.vlfl.campos.domain.usecase.GetUserCurrentPlaygroundUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class PlaygroundViewModel(
    val model: PlaygroundUiModel,
    private val getActivePlayersUseCase: GetActivePlayersUseCase,
    private val getUserCurrentPlaygroundUseCase: GetUserCurrentPlaygroundUseCase,
    private val checkInCurrentUserUseCase: CheckInCurrentUserUseCase
) : ViewModel() {

    private val _activePlayers = MutableSharedFlow<List<User>>(replay = CACHED_OBJECTS_NUMBER, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val activePlayers: SharedFlow<List<User>> = _activePlayers.asSharedFlow()

    private val _currentPlayground = MutableSharedFlow<UserCurrentPlayground?>(1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val currentPlayground: SharedFlow<UserCurrentPlayground?> = _currentPlayground.asSharedFlow()

    private val userID = FirebaseAuth.getInstance().currentUser?.uid

    init {
        getActivePlayers(model.id)
        subscribeToCurrentPlaygroundData()
    }

    private fun getActivePlayers(playgroundID: String?) {
        if (playgroundID != null) {
            viewModelScope.launch {
                _activePlayers.emitAll(getActivePlayersUseCase(playgroundId = playgroundID))
            }
        }
    }

    fun checkInCurrentUser() {
        if (model.id != null && model.name != null && userID != null) {
            viewModelScope.launch {
                checkInCurrentUserUseCase(userID, model.id, model.name)
            }
        }
    }

    private fun subscribeToCurrentPlaygroundData() {
        if (userID != null) {
            viewModelScope.launch {
                _currentPlayground.emitAll(getUserCurrentPlaygroundUseCase(userID))
            }
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("playgroundModel") private val model: PlaygroundUiModel,
        private val getActivePlayersUseCase: GetActivePlayersUseCase,
        private val getUserCurrentPlaygroundUseCase: GetUserCurrentPlaygroundUseCase,
        private val checkInCurrentUser: CheckInCurrentUserUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == PlaygroundViewModel::class.java)
            return PlaygroundViewModel(model, getActivePlayersUseCase, getUserCurrentPlaygroundUseCase, checkInCurrentUser) as T
        }

        @AssistedFactory
        interface AssistedInjectFactory {
            fun create(@Assisted("playgroundModel") model: PlaygroundUiModel): Factory
        }
    }

    companion object {
        private const val CACHED_OBJECTS_NUMBER = 10
    }
}
