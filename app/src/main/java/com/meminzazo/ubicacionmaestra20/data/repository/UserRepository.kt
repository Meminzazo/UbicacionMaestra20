package com.meminzazo.ubicacionmaestra20.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.meminzazo.ubicacionmaestra20.data.model.LocationPoint
import com.meminzazo.ubicacionmaestra20.data.model.User
import com.meminzazo.ubicacionmaestra20.data.model.UserRealtimeDB
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val realtimeDB: FirebaseDatabase,
    private val auth: FirebaseAuth
) {
    // Creacion de documento del usuario en Firestore
    suspend fun createUserFirestore(uid: String, email: String): Result<Unit> =
        try {
            val user = User(email = email)
            firestore
                .collection("users")
                .document(uid)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }

    // Creacion de nodo del usuario en Realtime Database
    suspend fun createUserRealtimeDB(uid: String): Result<Unit> =
        try {
            Log.d("UserRepository", "Creando registro el RealtimeDB")
            val user = UserRealtimeDB()
            realtimeDB
                .reference
                .child("users")
                .child(uid)
                .setValue(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception){
            Log.e("UserRepository", "e")
            Result.failure(e)
        }

    // Guardar historial de ubicaciones en Firestore

    suspend fun saveLocationHistory(
        uid: String,
        locationPoint: LocationPoint
    ): Result<Unit> =
        try{
            val fecha = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale("es", "MX")
            ).format(Date())
            firestore
                .collection("users")
                .document(uid)
                .collection("historial")
                .document(fecha)
                .update(
                    "puntos",
                    FieldValue.arrayUnion(locationPoint)
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }

    // Obtener el perfil del usuario de Firestore
    suspend fun getUserProfile(uid: String): Result<User> =
        try {
            val document = firestore
                .collection("users")
                .document(uid)
                .get()
                .await()
            val user = document.toObject(User::class.java)
            if (user != null){
                Result.success(user)
            } else{
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }

    fun getUserProfileFlow(uid: String): Flow<User?> =
        callbackFlow {
            val listener = firestore
                .collection("users")
                .document(uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null){
                        close(error)
                        return@addSnapshotListener
                    }
                    val user = snapshot?.toObject(User::class.java)
                    trySend(user)
                }
            awaitClose { listener.remove() }
        }

    suspend fun updateUserProfile(
        uid: String,
        nombres: String,
        apellidos: String,
        telefono: String
    ): Result<Unit> =
        try {
            firestore
                .collection("users")
                .document(uid)
                .update(
                    mapOf(
                        "nombres" to nombres,
                        "apellidos" to apellidos,
                        "telefono" to telefono
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
}