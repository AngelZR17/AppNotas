package com.kirodev.notasapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kirodev.notasapp.ui.screens.AddNoteScreen
import com.kirodev.notasapp.ui.screens.AddTaskScreen
import com.kirodev.notasapp.ui.screens.FavoritesScreen
import com.kirodev.notasapp.ui.screens.NotesScreen
import com.kirodev.notasapp.ui.screens.TaskScreen
import com.kirodev.notasapp.util.Preferences

@Composable
fun Navigation(ctx:Context){
    val navController = rememberNavController()
    LaunchedEffect(true) {
         if(Preferences(ctx).obtenerString("pantalla","").equals("hometasks")) {
            navController.navigate(AppScreens.TasksScreen.route)
        }   else if(Preferences(ctx).obtenerString("pantalla","").equals("homefavorites")) {
            navController.navigate(AppScreens.FavoritesScreen.route)
        }
    }
    NavHost(
        navController = navController,
        startDestination = AppScreens.NotesScreen.route
    ){
        composable(route = AppScreens.NotesScreen.route) {
            NotesScreen(ctx,navController)
        }
        composable(route = AppScreens.TasksScreen.route) {
            TaskScreen(ctx, navController)
        }
        composable(route = AppScreens.FavoritesScreen.route) {
            FavoritesScreen(ctx, navController)
        }
        composable(route = AppScreens.AddNote.route) {
            AddNoteScreen(ctx, navController)
        }
        composable(route = AppScreens.AddTask.route) {
            AddTaskScreen(ctx, navController)
        }
    }
}