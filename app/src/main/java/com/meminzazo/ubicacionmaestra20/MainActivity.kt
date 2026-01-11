package com.meminzazo.ubicacionmaestra20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.meminzazo.ubicacionmaestra20.ui.navigation.AppNavigation
import com.meminzazo.ubicacionmaestra20.ui.theme.UbicacionMaestra20Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbicacionMaestra20Theme {
                AppNavigation()
            }
        }
    }
}