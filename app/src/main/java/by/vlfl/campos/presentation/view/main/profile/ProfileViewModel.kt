package by.vlfl.campos.presentation.view.main.profile

import androidx.lifecycle.LiveData
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.lifecycle.emptySingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ProfileViewModel @Inject constructor() {

    private val _logoutEvent: SingleLiveEvent<Nothing> = emptySingleLiveEvent()
    val logoutEvent: LiveData<Nothing>
        get() = _logoutEvent

    fun getUserName() = FirebaseAuth.getInstance().currentUser?.displayName ?: throw IllegalStateException("No name specified for user")

    fun logOut() = _logoutEvent.emit()

}