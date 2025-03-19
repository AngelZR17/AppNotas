package com.kirodev.notasapp.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kirodev.notasapp.NotesViewModel
import com.kirodev.notasapp.TaskViewModel
import com.kirodev.notasapp.ui.screens.AddNoteScreen
import com.kirodev.notasapp.ui.screens.AddTaskScreen
import com.kirodev.notasapp.ui.screens.EditNoteScreen
import com.kirodev.notasapp.ui.screens.FavoritesScreen
import com.kirodev.notasapp.ui.screens.NotesScreen
import com.kirodev.notasapp.ui.screens.Settings
import com.kirodev.notasapp.ui.screens.TaskScreen
import com.kirodev.notasapp.util.Preferences

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    taskViewModel: TaskViewModel,
    notesViewModel: NotesViewModel,
    ctx:Context
) {
    val navController = rememberNavController()
    LaunchedEffect(true) {
        if (Preferences(ctx).obtenerString("pantalla","").equals("hometasks")) {
            navController.navigate(AppScreens.TasksScreen.route)
        } else if (Preferences(ctx).obtenerString("pantalla", "").equals("homefavorites")) {
            navController.navigate(AppScreens.FavoritesScreen.route)
        }
    }
    NavHost(
        navController = navController,
        startDestination = AppScreens.NotesScreen.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 1))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 1))
        }) {
        composable(route = AppScreens.NotesScreen.route) {
            val notes by notesViewModel.notes.observeAsState(emptyList())
            NotesScreen(notes, notesViewModel, ctx, navController)
        }
        composable(route = AppScreens.TasksScreen.route) {
            val tasks by taskViewModel.tasks.observeAsState(emptyList())
            TaskScreen(tasks, taskViewModel, ctx, navController)
        }
        composable(route = AppScreens.FavoritesScreen.route) {
            FavoritesScreen(ctx, navController)
        }
        composable(route = AppScreens.AddNote.route) {
            AddNoteScreen(notesViewModel, ctx, navController)
        }
        composable(route = AppScreens.EditNote.route) {
            EditNoteScreen(notesViewModel, ctx, navController)
        }
        composable(route = AppScreens.AddTask.route) {
            AddTaskScreen(ctx, navController)
        }
        composable(route = AppScreens.Settings.route) {
            Settings(ctx, navController)
        }
    }
}