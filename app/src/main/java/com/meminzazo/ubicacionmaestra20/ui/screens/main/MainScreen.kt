package com.meminzazo.ubicacionmaestra20.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.meminzazo.ubicacionmaestra20.ui.navigation.Routes
import com.meminzazo.ubicacionmaestra20.ui.screens.home.HomeScreen

// Clase que representa cada tab del BottomNav
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
){
    object Map : BottomNavItem(
        route = Routes.Home.route,
        label = "Mapa",
        icon = Icons.Default.Place
    )

    object Group : BottomNavItem(
        route = Routes.Home.route,
        label = "Grupo",
        icon = Icons.Default.Person
    )

    object History : BottomNavItem(
        route = Routes.History.route,
        label = "History",
        icon = Icons.Default.DateRange
    )
}

@Composable
fun MainScreen(){
    // Este navController es SOLO para las pantallas principales
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Map,
        BottomNavItem.Group,
        BottomNavItem.History
    )

    Scaffold(
        topBar = {
            MainTopBar(
                //Datos manualmente puestos
                userName = "Usuario",
                isSharing = false,
                onProfileClick = {},
                onSettingsClick = {}
            )
        },
        bottomBar = {
            NavigationBar {
                // Detecta en qué pantalla estás actualmente
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        // Se marca como seleccionado si estás en esa ruta
                        selected = currentDestination?.hierarchy?.any{
                            it.route == item.route
                        } == true,
                        onClick = {
                            navController.navigate(item.route){
                                // Evita acumular pantallas al navegar
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ){
            composable(Routes.Home.route){
                HomeScreen() { }
            }
            composable(Routes.Group.route){
                // GroupScreen()
                Text("Grupo")
            }
            composable(Routes.History.route){
                // HistoryScreen
                Text("Historial")
            }
        }
    }
}