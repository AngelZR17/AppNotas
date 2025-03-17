package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kirodev.notasapp.NotesViewModel
import com.kirodev.notasapp.data.Notes
import com.kirodev.notasapp.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(notes: List<Notes>, notesViewModel: NotesViewModel, ctx: Context, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text("Notas", color = MaterialTheme.colorScheme.onSecondary) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreens.AddNote.route)
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(Icons.Default.Create, contentDescription = "Agregar", tint = MaterialTheme.colorScheme.onSecondary)
            }
        },
        bottomBar = {
            NavBar(ctx = ctx, navController = navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if(notes.isNotEmpty()){
                    LazyColumn(){
                        itemsIndexed(notes){index, note ->
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable { }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = note.title,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.displaySmall
                                        )
                                        Text(
                                            text = note.note,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = note.dateUpdated,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Box(modifier = Modifier.size(72.dp)
                                    ) {
                                        IconButton(
                                            onClick = {

                                            },
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                        ) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Borrar",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(40.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay ninguna nota agregada",
                            style = MaterialTheme.typography.titleLarge,
                            //textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}