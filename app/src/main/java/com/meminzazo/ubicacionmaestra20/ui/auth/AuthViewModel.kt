package com.meminzazo.ubicacionmaestra20.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext
import com.meminzazo.ubicacionmaestra20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _authState = mutableStateOf<AuthState>(AuthState.Loading)
    val authState: AuthState get() = _authState.value

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                checkOnboardingStatus(auth.currentUser!!.uid)
                //AuthState.Authenticated
            } else {
                _authState.value = AuthState.Unauthenticated
                //AuthState.Unauthenticated
            }
    }

    private fun checkOnboardingStatus(uid: String){
        viewModelScope.launch {
            userRepository.getUserProfileFlow(uid)
                .collect { user ->
                    _authState.value = when {
                        user == null -> AuthState.Unauthenticated
                        user.nombres.isEmpty() -> AuthState.NeedsProfile
                        user.grupoId == null -> AuthState.NeedsGroup
                        else -> AuthState.Authenticated
                    }
                }
        }
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun recheckOnboarding(){
        val uid = firebaseAuth.currentUser?.uid ?: return
        checkOnboardingStatus(uid)
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}
