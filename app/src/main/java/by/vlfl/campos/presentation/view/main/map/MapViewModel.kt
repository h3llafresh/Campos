package by.vlfl.campos.presentation.view.main.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.lifecycle.SingleLiveEvent
import by.vlfl.campos.lifecycle.emit
import by.vlfl.campos.lifecycle.emptySingleLiveEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class MapViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var playgroundsReference: DatabaseReference

    val playgrounds :MutableList<Playground> = mutableListOf()

    private val _updateMapEvent: SingleLiveEvent<List<Playground>> = emptySingleLiveEvent()
    val updateMapEvent: LiveData<List<Playground>> get() = _updateMapEvent

    private val postListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (playgroundSnapshot in snapshot.children) {
                val post = playgroundSnapshot.getValue<Playground>()
                if (post != null) {
                    playgrounds.add(post)
                    Log.d("Data", playgrounds.joinToString())
                }
            }
            _updateMapEvent.emit(playgrounds)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MapViewModel", error.toException())
        }
    }

    fun getDataFromDb() {
        playgroundsReference.addValueEventListener(postListener)
    }


}