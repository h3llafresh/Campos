package by.vlfl.campos.di

import by.vlfl.campos.presentation.view.main.profile.ProfileFragment
import dagger.Subcomponent

@Subcomponent
interface MainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): MainComponent
    }

    fun inject(profileFragment: ProfileFragment)
}