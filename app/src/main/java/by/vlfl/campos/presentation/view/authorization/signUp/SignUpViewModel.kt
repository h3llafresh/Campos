package by.vlfl.campos.presentation.view.authorization.signUp

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.usecase.RegisterUserDataUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpViewModel(
    private val registerUserDataUseCase: RegisterUserDataUseCase
) : ViewModel() {

    private val _emptyFirstNameEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val invalidFirstNameEvent: LiveData<Nothing> get() = _emptyFirstNameEvent

    private val _emptyLastNameEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val invalidLastNameEvent: LiveData<Nothing> get() = _emptyLastNameEvent

    private val _invalidEmailEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val invalidEmailEvent: LiveData<Nothing> get() = _invalidEmailEvent

    private val _invalidPasswordEvent: SingleLiveEvent<Nothing> = SingleLiveEvent()
    val invalidPasswordEvent: LiveData<Nothing> get() = _invalidPasswordEvent

    private val _successValidationEvent: SingleLiveEvent<SignUpCredentialsUiModel> = SingleLiveEvent()
    val successValidationEvent: LiveData<SignUpCredentialsUiModel> = _successValidationEvent

    fun validateInputs(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) = when {
        firstName.isEmpty() -> _emptyFirstNameEvent.emit()
        lastName.isEmpty() -> _emptyLastNameEvent.emit()
        !isEmailValid(email) -> _invalidEmailEvent.emit()
        !isPasswordValid(password) -> _invalidPasswordEvent.emit()
        else -> _successValidationEvent.emit(
            SignUpCredentialsUiModel(
                name = "$firstName $lastName",
                email = email,
                password = password
            )
        )
    }


    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String) = Pattern.compile(PASSWORD_REG_EXP_PATTERN).matcher(password).matches()

    fun registerUserData(uid: String, name: String) = viewModelScope.launch {
        registerUserDataUseCase(uid, name)
    }

    class Factory @Inject constructor(
        private val registerUserDataUseCase: RegisterUserDataUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SignUpViewModel::class.java)
            return SignUpViewModel(registerUserDataUseCase) as T
        }
    }

    companion object {
        private const val PASSWORD_REG_EXP_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@\$%^&()\\[\\]{:;<>,?/~_+\\-=|]).{8,64}$"
    }
}