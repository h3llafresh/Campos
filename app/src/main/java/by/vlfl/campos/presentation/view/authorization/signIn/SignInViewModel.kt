package by.vlfl.campos.presentation.view.authorization.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.usecase.IGetUserProfileDataUseCase
import by.vlfl.campos.domain.usecase.IRegisterUserDataUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.presentation.view.main.profile.ProfileUiModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel(
    private val getUserProfileDataUseCase: IGetUserProfileDataUseCase,
    private val registerUserDataUseCase: IRegisterUserDataUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _navigateToMainActivityEvent: SingleLiveEvent<ProfileUiModel> = SingleLiveEvent()
    val navigateToMainActivityEvent: LiveData<ProfileUiModel> get() = _navigateToMainActivityEvent

    fun checkUserAuthorization() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            provideUserLogin(currentUser.uid)
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

    private suspend fun registerUserData(userID: String, userName: String) = registerUserDataUseCase(userID, userName)

    class Factory @Inject constructor(
        private val getUserProfileDataUseCase: IGetUserProfileDataUseCase,
        private val registerUserDataUseCase: IRegisterUserDataUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SignInViewModel::class.java)
            return SignInViewModel(getUserProfileDataUseCase, registerUserDataUseCase) as T
        }
    }
}