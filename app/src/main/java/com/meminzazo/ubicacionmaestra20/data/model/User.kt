package com.meminzazo.ubicacionmaestra20.data.model

data class User(
    val email: String = "",
    val nombres: String = "",
    val apellidos: String = "",
    val telefono: String = "",
    val grupoId: String? = null,
    val photoUrl: String? = null,
    val chatId: String? = null
)
