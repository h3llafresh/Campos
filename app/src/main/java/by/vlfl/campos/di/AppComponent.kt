package by.vlfl.campos.di

import android.content.Context
import by.vlfl.campos.MainApp
import by.vlfl.campos.di.module.AppModule
import by.vlfl.campos.di.module.NetworkModule
import by.vlfl.campos.di.module.PlaygroundModule
import by.vlfl.campos.di.module.StorageModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, StorageModule::class, NetworkModule::class, PlaygroundModule::class])
interface AppComponent  {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(application: MainApp)
    fun mainComponent(): MainComponent.Builder
}