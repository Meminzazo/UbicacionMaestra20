package com.meminzazo.ubicacionmaestra20.core.validators

import android.util.Patterns
import com.meminzazo.ubicacionmaestra20.core.utils.hasInternetConnection

object RegisterValidator {

    fun validate(
        email: String,
        password: String,
        confirmPassword: String
    ): String? {

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return "Por favor, complete todos los campos"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Por favor, ingrese un correo electrónico válido"
        }

        if (password.length < 8) {
            return "La contraseña debe tener al menos 8 caracteres"
        }

        if (!password.any { it.isDigit() }) {
            return "La contraseña debe contener al menos un número"
        }

        if (!password.any { it.isUpperCase() }) {
            return "La contraseña debe contener al menos una letra mayúscula"
        }

        if (!password.any { it.isLowerCase() }) {
            return "La contraseña debe contener al menos una letra minúscula"
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return "La contraseña debe contener al menos un carácter especial"
        }

        if (password.any { it.isWhitespace() }) {
            return "La contraseña no puede contener espacios en blanco"
        }

        if (password != confirmPassword) {
            return "Las contraseñas no coinciden"
        }

        return null
    }
}
