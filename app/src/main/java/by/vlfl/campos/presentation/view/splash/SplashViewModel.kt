package by.vlfl.campos.presentation.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.usecase.IGetUserProfileDataUseCase
import by.vlfl.campos.domain.usecase.IRegisterUserDataUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.presentation.view.main.profile.ProfileUiModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel(
    private val getUserProfileDataUseCase: IGetUserProfileDataUseCase,
    private val registerUserDataUseCase: IRegisterUserDataUseCase
) : ViewModel() {

    private val auth = Firebase.auth

    private val _navigateToMainActivityEvent: SingleLiveEvent<ProfileUiModel> = SingleLiveEvent()
    val navigateToMainActivityEvent: LiveData<ProfileUiModel> get() = _navigateToMainActivityEvent

    private val _navigateToSignInEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val navigateToSignInEvent: LiveData<Nothing> get() = _navigateToSignInEvent

    fun checkUserAuthorization() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            provideUserLogin(currentUser.uid)
        } else {
            _navigateToSignInEvent.emit()
        }
    }

    private fun provideUserLogin(userID: String) {
        viewModelScope.launch {
            val userProfileData = getUserProfileDataUseCase(userID)
            if (userProfileData != null) {
                _navigateToMainActivityEvent.emit(
                    with(userProfileData) {
                        ProfileUiModel(
                            id = id,
                            name = name,
                            birthDate = birthDate
                        )
                    }
                )
            } else {
                registerUserData(userID, auth.currentUser?.displayName!!)
                provideUserLogin(userID)
            }
        }
    }

    private suspend fun registerUserData(userID: String, userName: String) {
        registerUserDataUseCase(userID, userName)
    }

    class Factory @Inject constructor(
        private val getUserProfileDataUseCase: IGetUserProfileDataUseCase,
        private val registerUserDataUseCase: IRegisterUserDataUseCase
        ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(getUserProfileDataUseCase, registerUserDataUseCase) as T
        }
    }
}