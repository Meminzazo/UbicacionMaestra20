package com.meminzazo.ubicacionmaestra20.data.repository

import com.meminzazo.ubicacionmaestra20.data.model.Group
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){
    private fun generateGroupCode(): String {
        val chars = ('A'..'Z') + ('0'..'9')
        return buildString {
            repeat(3){ block ->
                if (block > 0) append('-')
                repeat(4) { append(chars.random()) }
            }
        }
    }
    suspend fun createGroup(uid: String): Result<Unit> {
        return try {
            val groupCode = generateGroupCode()
            val group = Group(
                creadoPor = uid,
                miembros = listOf(uid)
            )
            firestore
                .collection("grupos")
                .document(groupCode)
                .set(group)
                .await()

            firestore
                .collection("users")
                .document(uid)
                .update("grupoId", groupCode)
                .await()

            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun joinGroup(uid: String, groupCode: String): Result<Unit> {
        return try {
            val groupDoc = firestore
                .collection("grupos")
                .document(groupCode)
                .get()
                .await()

            if (!groupDoc.exists()){
                return Result.failure(Exception("El grupo no existe"))
            }

            val miembros = groupDoc.get("miembros") as? List<*> ?: emptyList<String>()
            if (miembros.size >= 7) {
                return Result.failure(Exception("El grupo está lleno"))
            }

            firestore
                .collection("grupos")
                .document(groupCode)
                .update("miembros", miembros + uid)
                .await()

            firestore
                .collection("users")
                .document(uid)
                .update("grupoId", groupCode)
                .await()

            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
