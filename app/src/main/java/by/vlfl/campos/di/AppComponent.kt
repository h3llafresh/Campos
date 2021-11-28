package by.vlfl.campos.di

import android.content.Context
import by.vlfl.campos.MainApp
import by.vlfl.campos.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, StorageModule::class, NetworkModule::class, PlaygroundModule::class, UserModule::class])
interface AppComponent  {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(application: MainApp)
    fun mainComponent(): MainComponent.Builder
    fun splashComponent(): SplashComponent.Builder
}