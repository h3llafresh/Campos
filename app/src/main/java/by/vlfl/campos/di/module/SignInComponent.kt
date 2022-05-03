package by.vlfl.campos.di.module

import by.vlfl.campos.presentation.view.authorization.signIn.SignInFragment
import by.vlfl.campos.presentation.view.authorization.signUp.SignUpFragment
import dagger.Subcomponent

@Subcomponent
interface SignInComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): SignInComponent
    }

    fun inject(signInFragment: SignInFragment)
    fun inject(signUpFragment: SignUpFragment)
}