package by.vlfl.campos.di

import by.vlfl.campos.presentation.view.main.filter.FilterFragment
import by.vlfl.campos.presentation.view.main.map.MapFragment
import by.vlfl.campos.presentation.view.main.playground.PlaygroundFragment
import by.vlfl.campos.presentation.view.main.profile.ProfileFragment
import by.vlfl.campos.presentation.view.main.qrConfirmation.QrConfirmationFragment
import dagger.Subcomponent

@Subcomponent
interface MainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): MainComponent
    }

    fun inject(profileFragment: ProfileFragment)
    fun inject(mapFragment: MapFragment)
    fun inject(filterFragment: FilterFragment)
    fun inject(playgroundFragment: PlaygroundFragment)
    fun inject(qrConfirmationFragment: QrConfirmationFragment)
}