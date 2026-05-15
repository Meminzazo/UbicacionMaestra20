package com.meminzazo.ubicacionmaestra20.data.model

data class Group(
    val creadoPor: String = "",
    val creadoEn: Long = System.currentTimeMillis(),
    val miembros: List<String> = emptyList()
)