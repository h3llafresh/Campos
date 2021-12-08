package by.vlfl.campos.di.module

import by.vlfl.campos.presentation.view.signIn.SignInActivity
import dagger.Subcomponent

@Subcomponent
interface SignInComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): SignInComponent
    }

    fun inject(signInActivity: SignInActivity)
}