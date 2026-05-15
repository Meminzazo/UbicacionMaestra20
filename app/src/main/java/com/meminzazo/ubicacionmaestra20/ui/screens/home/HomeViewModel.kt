package com.meminzazo.ubicacionmaestra20.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.meminzazo.ubicacionmaestra20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeUiState(
    val userLocation: LatLng= LatLng(19.4326, -99.1332),
    val isLocationLoading: Boolean = false,
    val nombre: String = "",
    val isSharing: Boolean = false,
    val errorMessage: String? = null
)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile(){
        viewModelScope.launch{
            val uid = firebaseAuth.currentUser?.uid
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}

