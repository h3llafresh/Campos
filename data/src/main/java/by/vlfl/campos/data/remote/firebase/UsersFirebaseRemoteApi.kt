package by.vlfl.campos.data.remote.firebase

import android.util.Log
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UsersFirebaseRemoteApi @Inject constructor() {
    @Singleton
    @Inject
    @Named("Users")
    lateinit var usersRemoteReference: DatabaseReference

    @Singleton
    @Inject
    @Named("Playgrounds")
    lateinit var playgroundsRemoteReference: DatabaseReference

    private val userCurrentPlaygroundDataFlow = MutableSharedFlow<Playground?>()

    suspend fun getUserData(userID: String): User {
        return withContext(Dispatchers.IO) {
            val task = usersRemoteReference.child(userID).get()
            Tasks.await(task).getValue<User>()
                ?: throw (IllegalStateException("User with such ID wasn't retrieved"))
        }
    }

    suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String) {
        withContext(Dispatchers.IO) {
            val playgroundDataMap = mapOf("name" to playgroundName)
            usersRemoteReference.child(userID).child("currentPlayground").setValue(mapOf(playgroundID to playgroundDataMap))
            val user = getUserData(userID)
            val userDataMap = mapOf("name" to user.name)
            playgroundsRemoteReference.child(playgroundID).child("activePlayers").updateChildren(
                mapOf(userID to userDataMap)
            )
        }
    }

    fun subscribeToUserCurrentPlayground(userID: String): Flow<Playground?> {
        setUserPlaygroundDataChangeListener(userID)
        return userCurrentPlaygroundDataFlow
    }

    private fun setUserPlaygroundDataChangeListener(userID: String) {
        usersRemoteReference.child(userID).child("currentPlayground").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        for (playgroundSnapshot in snapshot.children) {
                            val currentPlayground = playgroundSnapshot.getValue<Playground>()?.copy(id = playgroundSnapshot.key)
                            userCurrentPlaygroundDataFlow.emit(currentPlayground)
                            Log.d("User playground data", currentPlayground.toString())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("UserCurrPground", error.toException())
                }
            }
        )
    }

    fun leaveCurrentGame(userID: String, playgroundID: String) {
        usersRemoteReference.child(userID).child("currentPlayground").child(playgroundID).child("name").setValue("")
        playgroundsRemoteReference.child(playgroundID).child("activePlayers").child(userID).child("name").setValue("")
    }
}