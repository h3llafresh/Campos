package by.vlfl.campos.di.module

import by.vlfl.campos.di.MainComponent
import by.vlfl.campos.di.SplashComponent
import dagger.Module

@Module(subcomponents = [MainComponent::class, SplashComponent::class, SignInComponent::class])
class AppModule