package com.meminzazo.ubicacionmaestra20.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meminzazo.ubicacionmaestra20.ui.auth.AuthState
import com.meminzazo.ubicacionmaestra20.ui.auth.AuthViewModel
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.AuthMenuScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.login.LoginScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.recovery.RecoveryScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.register.RegisterScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.home.HomeScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState = authViewModel.authState

    when (authState) {

        AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        AuthState.Authenticated -> {
            HomeScreen()
        }

        AuthState.Unauthenticated -> {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Routes.AuthMenu.route
            ) {

                composable(Routes.AuthMenu.route) {
                    AuthMenuScreen(
                        onLoginClick = {
                            navController.navigate(Routes.Login.route)
                        },
                        onRegisterClick = {
                            navController.navigate(Routes.Register.route)
                        }
                    )
                }

                composable(Routes.Login.route) {
                    LoginScreen(
                        onForgotPassword = {
                            navController.navigate(Routes.Recovery.route)
                        }
                    )
                }

                composable(Routes.Register.route) {
                    RegisterScreen()
                }

                composable(Routes.Recovery.route) {
                    RecoveryScreen(
                        onNavigateBack = {
                            navController.navigate(Routes.AuthMenu.route) {
                                popUpTo(Routes.AuthMenu.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
