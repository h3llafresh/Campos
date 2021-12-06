package by.vlfl.campos.data.remote.firebase

import by.vlfl.campos.domain.entity.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
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

    suspend fun getUserData(userID: String): User {
        return withContext(Dispatchers.IO) {
            val task = usersRemoteReference.child(userID).get()
            Tasks.await(task).getValue<User>()
                ?: throw (IllegalStateException("User with such ID wasn't retrieved"))
        }
    }

    suspend fun checkInCurrentUser(userID: String, playgroundID: String, playgroundName: String) {
        withContext(Dispatchers.IO) {
            usersRemoteReference.child(userID).child("currentPlayground").setValue(mapOf(playgroundID to playgroundName))
            val user = getUserData(userID)
            val userDataMap = mapOf("name" to user.name)
            playgroundsRemoteReference.child(playgroundID).child("activePlayers").updateChildren(
                mapOf(userID to userDataMap)
            )
        }
    }
}