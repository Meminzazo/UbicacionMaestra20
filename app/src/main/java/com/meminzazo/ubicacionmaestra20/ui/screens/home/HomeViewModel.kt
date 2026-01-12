package com.meminzazo.ubicacionmaestra20.ui.screens.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun logout() {
        firebaseAuth.signOut()
    }
}
