package com.meminzazo.ubicacionmaestra20.data.model

data class UserRealtimeDB(
    val nombre: String = "",
    val latitud: String = "",
    val longiud: String = "",
    val timestamp: Long = 0,
    val isSharing: Boolean = false
)
