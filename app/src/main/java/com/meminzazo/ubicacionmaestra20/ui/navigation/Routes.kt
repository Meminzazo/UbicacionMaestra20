package com.meminzazo.ubicacionmaestra20.ui.navigation

sealed class Routes(val route: String) {
    //Auth
    object AuthMenu : Routes("auth_menu")
    object Login : Routes("login")
    object Register : Routes("register")
    object Recovery : Routes("recovery")

    //Menu
    object Home : Routes("home")
    object Group : Routes("group")
    object History : Routes("history")
}
