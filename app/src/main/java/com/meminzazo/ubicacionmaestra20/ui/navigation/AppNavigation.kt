package com.meminzazo.ubicacionmaestra20.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.AuthMenuScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.LoginScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.RecoveryScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.auth.RegisterScreen
import com.meminzazo.ubicacionmaestra20.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
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
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.AuthMenu.route) {
                            inclusive = true
                        }
                    }
                },
                onForgotPassword = {
                    navController.navigate(Routes.Recovery.route){
                        popUpTo(Routes.Login.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.AuthMenu.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(
                onSingOutClick = {
                    navController.navigate(Routes.AuthMenu.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Routes.Recovery.route){
            RecoveryScreen()
        }
    }

}
