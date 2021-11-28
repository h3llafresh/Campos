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

    suspend fun getUserData(userID: String): User {
        return withContext(Dispatchers.IO) {
            val task = usersRemoteReference.child(userID).get()
            Tasks.await(task).getValue<User>()
                ?: throw (IllegalStateException("User with such ID wasn't retrieved"))
        }
    }
}