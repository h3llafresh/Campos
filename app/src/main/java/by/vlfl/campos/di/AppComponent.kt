package by.vlfl.campos.di

import android.content.Context
import by.vlfl.campos.MainApp
import by.vlfl.campos.di.module.AppModule
import by.vlfl.campos.di.module.NetworkModule
import by.vlfl.campos.di.module.SignInComponent
import by.vlfl.campos.di.module.playground.PlaygroundBindModule
import by.vlfl.campos.di.module.playground.PlaygroundModule
import by.vlfl.campos.di.module.user.UserBindModule
import by.vlfl.campos.di.module.user.UserModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, PlaygroundModule::class, PlaygroundBindModule::class, UserModule::class, UserBindModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(application: MainApp)
    fun mainComponent(): MainComponent.Builder
    fun splashComponent(): SplashComponent.Builder
    fun signInComponent(): SignInComponent.Builder
}