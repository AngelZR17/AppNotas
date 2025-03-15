package com.kirodev.notasapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val nombre: String,
    val ruta: String,
    val iconoSeleccionado: ImageVector,
    val iconoDeseleccionado: ImageVector,

)

val bottomNavItems = listOf(
    BottomNavItem(
        nombre = "Notas",
        ruta = "homenotes",
        iconoSeleccionado = Icons.AutoMirrored.Filled.StickyNote2,
        iconoDeseleccionado = Icons.AutoMirrored.Outlined.StickyNote2,
    ),
    BottomNavItem(
        nombre = "Tareas",
        ruta = "hometasks",
        iconoSeleccionado = Icons.Filled.Task,
        iconoDeseleccionado = Icons.Outlined.Task,
    ),
    BottomNavItem(
        nombre = "Favoritos",
        ruta = "homefavorites",
        iconoSeleccionado = Icons.Filled.Star,
        iconoDeseleccionado = Icons.Outlined.StarOutline,
    ),
)