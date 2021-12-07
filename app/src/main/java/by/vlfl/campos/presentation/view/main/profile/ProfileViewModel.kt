package by.vlfl.campos.presentation.view.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.usecase.GetUserCurrentPlayground
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.lifecycle.emptySingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(getUserCurrentPlayground: GetUserCurrentPlayground) : ViewModel() {

    private val _logoutEvent: SingleLiveEvent<Nothing> = emptySingleLiveEvent()
    val logoutEvent: LiveData<Nothing>
        get() = _logoutEvent

    private val _currentPlayground = MutableSharedFlow<Playground?>()
    val currentPlayground: SharedFlow<Playground?> = _currentPlayground.asSharedFlow()

    init {
        viewModelScope.launch {
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            if (userID != null) {
                _currentPlayground.emitAll(getUserCurrentPlayground(userID))
            }
        }
    }

    fun getUserName() = FirebaseAuth.getInstance().currentUser?.displayName ?: throw IllegalStateException("No name specified for user")

    fun logOut() = _logoutEvent.emit()

    class Factory @Inject constructor(
        private val getUserCurrentPlayground: GetUserCurrentPlayground
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == ProfileViewModel::class.java)
            return ProfileViewModel(getUserCurrentPlayground) as T
        }
    }
}
