package by.vlfl.campos.presentation.view.main.qrConfirmation

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.vlfl.campos.domain.usecase.CheckInCurrentUserUseCase
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlin.math.abs

class QrConfirmationViewModel(
    val model: QrConfirmationUiModel,
    private val checkInCurrentUserUseCase: CheckInCurrentUserUseCase
) : ViewModel() {

    private val _confirmationResultEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val confirmationResultEvent: LiveData<Boolean>
        get() = _confirmationResultEvent

    fun checkUserIn(playgroundID: String, playgroundName: String, currentLocation: Location, playgroundCoordinates: LatLng) {
        if (isUserOnPlayground(currentLocation, playgroundCoordinates)) {
            viewModelScope.launch {
                checkInCurrentUserUseCase(
                    userID = model.userID,
                    playgroundID = playgroundID,
                    playgroundName = playgroundName
                )
                _confirmationResultEvent.emit(true)
            }
        } else {
            _confirmationResultEvent.emit(false)
        }
    }

    private fun isUserOnPlayground(userLocation: Location, playgroundCoordinates: LatLng): Boolean {
        return compareUserAndPlaygroundCoordinates(
            userLocation.latitude,
            playgroundCoordinates.latitude
        ) && compareUserAndPlaygroundCoordinates(
            userLocation.longitude,
            playgroundCoordinates.longitude
        )
    }

    private fun compareUserAndPlaygroundCoordinates(userCoordinate: Double, playgroundCoordinate: Double): Boolean {
        return abs(userCoordinate - playgroundCoordinate) < 0.5
    }

    class Factory @AssistedInject constructor(
        @Assisted("qrConfirmationUiModel") private val model: QrConfirmationUiModel,
        private val checkInCurrentUserUseCase: CheckInCurrentUserUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == QrConfirmationViewModel::class.java)
            return QrConfirmationViewModel(model, checkInCurrentUserUseCase) as T
        }

        @AssistedFactory
        interface AssistedInjectFactory {
            fun create(@Assisted("qrConfirmationUiModel") model: QrConfirmationUiModel): Factory
        }
    }
}