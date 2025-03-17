package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kirodev.notasapp.NotesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(notesViewModel: NotesViewModel, ctx: Context, navController: NavController){
    val currentTitle = rememberSaveable { mutableStateOf("") }
    val currentDescription = rememberSaveable { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                        title = { Text("Agregar Nota", color = MaterialTheme.colorScheme.onSecondary) },
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.AttachFile, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Mic, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                            }
                        },
                        Modifier.windowInsetsPadding(WindowInsets.ime),
                        floatingActionButton = {
                            if(currentTitle.value.isNotBlank()){
                                FloatingActionButton(
                                    onClick = {
                                        notesViewModel.createNote(
                                            currentTitle.value,
                                            currentDescription.value.ifEmpty { "Sin descripción" } ?: "Sin descripción"
                                        )
                                        navController.popBackStack()
                                    },
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.onSecondary)
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                },
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        Column {
                            TextField(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                ),
                                value = currentTitle.value,
                                onValueChange = { value ->
                                    currentTitle.value = value
                                },
                                placeholder = { Text("Título") }
                            )
                            TextField(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).height(200.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                ),
                                value = currentDescription.value,
                                onValueChange = { value ->
                                    currentDescription.value = value
                                },
                                placeholder = { Text("Descripción") }
                            )
                        }
                    }
                }
            )
        }
    }
}