package by.vlfl.campos.data.remote.firebase

import android.util.Log
import by.vlfl.campos.domain.entity.Playground
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PlaygroundsFirebaseRemoteApi @Inject constructor() {
    @Inject
    @Named("Playgrounds")
    lateinit var playgroundsRemoteReference: DatabaseReference

    private val playgroundsDataFlow = MutableSharedFlow<Playground>()

    fun subscribeToPlaygroundsDataChangeEvent(): SharedFlow<Playground> {
        setPlaygroundsDataChangeListener()
        return playgroundsDataFlow
    }

    private fun setPlaygroundsDataChangeListener() {
        playgroundsRemoteReference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        for (playgroundSnapshot in snapshot.children) {
                            val playgroundInfo = playgroundSnapshot.getValue<Playground>()
                            if (playgroundInfo != null) {
                                playgroundsDataFlow.emit(playgroundInfo)
                                Log.d("Playgrounds Data", playgroundInfo.toString())
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("PlaygroundsDataChange", error.toException())
                }
            }
        )
    }
}