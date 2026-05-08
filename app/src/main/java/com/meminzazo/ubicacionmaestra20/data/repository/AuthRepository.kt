package com.meminzazo.ubicacionmaestra20.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.meminzazo.ubicacionmaestra20.data.remote.firebase.FirebaseAuthDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remote: FirebaseAuthDataSource
) {

    suspend fun login(email: String, password: String): Result<Unit> =
        try {
            Log.d("LOGIN_DEBUG", "Repository login() llamado")
            remote.login(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("LOGIN_DEBUG", "Error en el repositorio: ${e.message}")
            Result.failure(e)
        }

    suspend fun register(email: String, password: String): Result<Unit> =
        try {
            remote.register(email, password)
            Result.success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Este correo ya está registrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        try {
            remote.sendPasswordResetEmail(email)
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.d("RECOVERY_DEBUG", "Error en el repositorio: ${e.message}")
            Result.failure(Exception("Este correo no está registrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }


    fun isUserLoggedIn(): Boolean{
        return remote.isUserLoggedIn()
    }
}
