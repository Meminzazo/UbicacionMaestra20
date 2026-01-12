package com.meminzazo.ubicacionmaestra20.core.validators

object LoginValidator {

    fun validate(
        email: String,
        password: String
    ): String? {
        if (email.isBlank() || password.isBlank()){
            return "Por favor, complete todos los campos"
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Por favor, ingrese un correo electrónico válido"
        }

        if (email.any { it.isWhitespace() }) {
            return "El correo electrónico no puede contener espacios"
        }

        if (password.any { it.isWhitespace() }) {
            return "La contraseña no puede contener espacios"
        }
        else return null
    }
}