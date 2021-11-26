package by.vlfl.campos.di.module

import by.vlfl.campos.utils.Constants.FIREBASE_DATABASE_REFERENCE
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideFirebaseRealTimeDatabase(): FirebaseDatabase {
        return Firebase.database(FIREBASE_DATABASE_REFERENCE)
    }

    @Singleton
    @Provides
    fun providePlaygroundsRemoteReference(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference()
    }
}