package by.vlfl.campos.presentation.view.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.entity.UserCurrentPlayground
import by.vlfl.campos.domain.usecase.GetUserCurrentPlaygroundUseCase
import by.vlfl.campos.domain.usecase.LeaveCurrentGameUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(
    getUserCurrentPlaygroundUseCase: GetUserCurrentPlaygroundUseCase,
    private val leaveCurrentGameUseCase: LeaveCurrentGameUseCase
) : ViewModel() {

    private val _logoutEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val logoutEvent: LiveData<Nothing>
        get() = _logoutEvent

    private val _currentPlayground = MutableStateFlow<UserCurrentPlayground?>(null)
    val currentPlayground: StateFlow<UserCurrentPlayground?> = _currentPlayground.asStateFlow()

    init {
        viewModelScope.launch {
            val userID = Firebase.auth.currentUser?.uid
            if (userID != null) {
                _currentPlayground.emitAll(getUserCurrentPlaygroundUseCase(userID))
            }
        }
    }

    fun getUserName() = Firebase.auth.currentUser?.displayName ?: throw IllegalStateException("No name specified for user")

    fun logOut() = _logoutEvent.emit()

    fun leaveCurrentGame() {
        val userID = Firebase.auth.currentUser?.uid
        val playgroundID = currentPlayground.replayCache.first()?.id
        if (userID != null && playgroundID != null) {
            viewModelScope.launch {
                leaveCurrentGameUseCase(userID, playgroundID)
            }
        }
    }

    class Factory @Inject constructor(
        private val getUserCurrentPlaygroundUseCase: GetUserCurrentPlaygroundUseCase,
        private val leaveCurrentGame: LeaveCurrentGameUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ProfileViewModel::class.java)
            return ProfileViewModel(getUserCurrentPlaygroundUseCase, leaveCurrentGame) as T
        }
    }
}
