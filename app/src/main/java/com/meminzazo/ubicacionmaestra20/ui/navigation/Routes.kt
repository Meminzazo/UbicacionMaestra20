package com.meminzazo.ubicacionmaestra20.ui.navigation

sealed class Routes(val route: String) {

    object AuthMenu : Routes("auth_menu")
    object Login : Routes("login")
    object Register : Routes("register")
    object Home : Routes("home")
    object Recovery : Routes("recovery")
}
