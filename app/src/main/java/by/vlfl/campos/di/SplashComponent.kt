package by.vlfl.campos.di

import by.vlfl.campos.presentation.view.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent
interface SplashComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): SplashComponent
    }

    fun inject(splashActivity: SplashActivity)
}