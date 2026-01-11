package com.meminzazo.ubicacionmaestra20.data.repository

import com.meminzazo.ubicacionmaestra20.data.remote.firebase.FirebaseAuthDataSource

class AuthRepository(
    private val remote: FirebaseAuthDataSource = FirebaseAuthDataSource()
) {

    suspend fun login(email: String, password: String): Result<Unit> =
        try {
            remote.login(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun register(email: String, password: String): Result<Unit> =
        try {
            remote.register(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    fun isUserLoggedIn(): Boolean = remote.isUserLoggedIn()
}
