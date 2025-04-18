package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(ctx: Context, navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text("Favoritos", color = MaterialTheme.colorScheme.onSecondary) },
            )
        },
        bottomBar = {
            NavBar(ctx = ctx, navController = navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    text = "Pantalla favoritos"
                )
            }
        }
    )
}