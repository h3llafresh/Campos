package by.vlfl.campos.presentation.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.usecase.GetUserProfileDataUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.lifecycle.emptySingleLiveEvent
import by.vlfl.campos.presentation.view.main.profile.ProfileModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel(private val getUserProfileDataUseCase: GetUserProfileDataUseCase) : ViewModel() {

    private val _navigateToMainActivityEvent: SingleLiveEvent<ProfileModel> = emptySingleLiveEvent()
    val navigateToMainActivityEvent: LiveData<ProfileModel> get() = _navigateToMainActivityEvent

    fun provideUserLogin(userID: String) {
        viewModelScope.launch {
            val userProfileData = getUserProfileDataUseCase(userID)
            _navigateToMainActivityEvent.emit(
                with(userProfileData) {
                    ProfileModel(
                        id = id ?: "",
                        name = name ?: "",
                        birthDate = birthDate ?: ""
                    )
                }
            )
        }
    }

    class Factory @Inject constructor(private val getUserProfileDataUseCase: GetUserProfileDataUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(getUserProfileDataUseCase) as T
        }
    }
}