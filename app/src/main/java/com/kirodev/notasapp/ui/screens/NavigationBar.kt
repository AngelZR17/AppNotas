package com.kirodev.notasapp.ui.screens

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kirodev.notasapp.navigation.bottomNavItems
import com.kirodev.notasapp.util.Preferences

@Composable
fun NavBar(ctx: Context, navController: NavController){
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    NavigationBar( containerColor = MaterialTheme.colorScheme.primary){
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (currentRoute == item.ruta) item.iconoSeleccionado else item.iconoDeseleccionado,
                        contentDescription = item.nombre
                    )
                },
                label = { Text(item.nombre) },
                selected = (currentRoute == item.ruta),
                onClick = {
                    if (navController.currentDestination?.route != item.ruta){
                        Preferences(ctx).guardarEntero("indexItem",index)
                        navController.navigate(item.ruta) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        Preferences(ctx).guardarString("pantalla",item.ruta)
                    }
                }
            )
        }
    }
}