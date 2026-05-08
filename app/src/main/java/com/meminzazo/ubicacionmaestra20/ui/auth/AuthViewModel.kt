package com.meminzazo.ubicacionmaestra20.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _authState = mutableStateOf<AuthState>(AuthState.Loading)
    val authState: AuthState get() = _authState.value

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        _authState.value =
            if (auth.currentUser != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun logout() {
        firebaseAuth.signOut()
        // NO navegamos, el listener se encarga
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}
