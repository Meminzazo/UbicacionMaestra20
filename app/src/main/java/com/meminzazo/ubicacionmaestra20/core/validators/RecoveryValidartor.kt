package com.meminzazo.ubicacionmaestra20.core.validators

object RecoveryValidartor {
    fun validate(
        email: String
    ): String? {
        if (email.isBlank()) {
            return "Por favor, ingrese su correo electrónico"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Por favor, ingrese un correo electrónico válido"
        }
        else return null
    }
}