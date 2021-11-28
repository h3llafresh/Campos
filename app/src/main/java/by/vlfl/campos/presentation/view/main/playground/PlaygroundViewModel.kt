package by.vlfl.campos.presentation.view.main.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PlaygroundViewModel(val model: PlaygroundModel) : ViewModel() {

    class Factory @AssistedInject constructor(
        @Assisted("playgroundModel")private val model: PlaygroundModel
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == PlaygroundViewModel::class.java)
            return PlaygroundViewModel(model) as T
        }

        @AssistedFactory
        interface AssistedInjectFactory {
            fun create(@Assisted("playgroundModel") model: PlaygroundModel): Factory
        }
    }
}