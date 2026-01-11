package com.meminzazo.ubicacionmaestra20.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun logout() {
        auth.signOut()
    }
}
