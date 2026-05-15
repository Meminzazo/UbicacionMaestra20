package com.meminzazo.ubicacionmaestra20.ui.auth

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()

    object NeedsProfile : AuthState()
    object NeedsGroup : AuthState()
    object Authenticated : AuthState()
}
