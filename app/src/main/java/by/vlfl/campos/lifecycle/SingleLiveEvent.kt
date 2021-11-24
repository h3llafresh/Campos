package by.vlfl.campos.lifecycle

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>(v: T? = null) : MutableLiveData<T>(v) {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w("SingleLiveEvent", "MultipleObservers registered but only one will be notified of changes and only once.")
        }

        super.observe(owner, { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }
}


@MainThread
fun SingleLiveEvent<Nothing>.emit() {
    this.value = null
}

fun SingleLiveEvent<Nothing>.postEmit() {
    this.postValue(null)
}

@MainThread
fun <T> SingleLiveEvent<T>.emit(value: T) {
    this.value = value
}

fun <T> emptySingleLiveEvent(): SingleLiveEvent<T> = SingleLiveEvent()