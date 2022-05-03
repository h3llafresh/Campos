package by.vlfl.campos.presentation.view.authorization.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.usecase.GetUserProfileDataUseCase
import by.vlfl.campos.domain.usecase.RegisterUserDataUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.presentation.view.main.profile.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel(
    private val getUserProfileDataUseCase: GetUserProfileDataUseCase,
    private val registerUserDataUseCase: RegisterUserDataUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _navigateToMainActivityEvent: SingleLiveEvent<ProfileModel> = SingleLiveEvent()
    val navigateToMainActivityEvent: LiveData<ProfileModel> get() = _navigateToMainActivityEvent

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
                        ProfileModel(
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
        private val getUserProfileDataUseCase: GetUserProfileDataUseCase,
        private val registerUserDataUseCase: RegisterUserDataUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SignInViewModel::class.java)
            return SignInViewModel(getUserProfileDataUseCase, registerUserDataUseCase) as T
        }
    }
}