package by.vlfl.campos.data.remote.firebase.playground.api

import android.util.Log
import by.vlfl.campos.data.remote.firebase.playground.PlaygroundDto
import by.vlfl.campos.data.remote.firebase.user.UserDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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

    private val playgroundsDataFlow = MutableSharedFlow<PlaygroundDto>()

    private val activePlayersDataFlow = MutableSharedFlow<List<UserDto>>()

    fun subscribeToActivePlayersDataChangeEvent(playgroundId: String): SharedFlow<List<UserDto>> {
        setActivePlayersDataChangeListener(playgroundId)
        return activePlayersDataFlow
    }

    fun subscribeToPlaygroundsDataChangeEvent(): SharedFlow<PlaygroundDto> {
        setPlaygroundsDataChangeListener()
        return playgroundsDataFlow
    }

    private fun setPlaygroundsDataChangeListener() {
        playgroundsRemoteReference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        for (playgroundSnapshot in snapshot.children) {
                            val activePlayers = playgroundSnapshot.child("activePlayers").children.mapNotNull { activePlayerSnapshot ->
                                activePlayerSnapshot?.let {
                                    it.getValue(UserDto::class.java)?.copy(id = activePlayerSnapshot.key)
                                }
                            }
                            val playgroundInfo = playgroundSnapshot.getValue(PlaygroundDto::class.java)
                                ?.copy(id = playgroundSnapshot.key, players = activePlayers.filter { !it.name.isNullOrEmpty() })
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

    private fun setActivePlayersDataChangeListener(playgroundId: String) {
        playgroundsRemoteReference.child(playgroundId).child("activePlayers").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val activePlayers = snapshot.children.mapNotNull { activePlayerSnapshot ->
                            activePlayerSnapshot?.let {
                                it.getValue(UserDto::class.java)?.copy(id = activePlayerSnapshot.key)
                            }
                        }
                        if (activePlayers.isNotEmpty()) {
                            activePlayersDataFlow.emit(activePlayers)
                            Log.d("Active Player Data list", activePlayers.joinToString { "" })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("ActivePlayerDataChange", error.toException())
                }
            }
        )
    }
}