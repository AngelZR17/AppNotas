package com.kirodev.notasapp.navigation

sealed class AppScreens (val route: String){
    object NotesScreen: AppScreens("homenotes")
    object TasksScreen: AppScreens("hometasks")
    object FavoritesScreen: AppScreens("homefavorites")
    object AddNote: AppScreens("addnote")
    object AddTask: AppScreens("addtask")
}
