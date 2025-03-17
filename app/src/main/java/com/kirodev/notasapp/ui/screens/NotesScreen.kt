package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kirodev.notasapp.NotesViewModel
import com.kirodev.notasapp.data.Notes
import com.kirodev.notasapp.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(notes: List<Notes>, notesViewModel: NotesViewModel, ctx: Context, navController: NavController) {
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Notes?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        bottomBar = {
            NavBar(ctx = ctx, navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (notes.isNotEmpty()) {
                LazyColumn {
                    items(notes, key = { it.id!! }) { note ->
                        NoteCard(
                            note = note,
                            onNoteClick = {

                            },
                            onNoteLongClick = {
                                selectedNote = note
                                showBottomSheet = true
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = "Icono de eliminar",
                        modifier = Modifier.padding(bottom = 15.dp).size(size = 40.dp)
                    )
                    Text(
                        text = "No hay notas aún",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
    if (selectedNote != null && showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                onEditClick = {
                    selectedNote?.let { note ->

                    }
                    showBottomSheet = false
                },
                onDeleteClick = {
                    showDeleteConfirmation = true
                    showBottomSheet = false
                },
                onShareClick = {
                    showBottomSheet = false
                }
            )
        }
    }
    if (showDeleteConfirmation) {
        ShowDeleteDialog(
            onConfirm = {
                selectedNote?.let { notesViewModel.deleteNotes(it) }
                showDeleteConfirmation = false
            },
            onCancel = { showDeleteConfirmation = false }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(
    note: Notes,
    onNoteClick: () -> Unit,
    onNoteLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = onNoteClick,
                onLongClick = onNoteLongClick
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
        }
    }
}

@Composable
private fun BottomSheetContent(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
            onClick = onEditClick
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Icono de editar",
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(text = "Editar nota")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
            onClick = onShareClick
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Icono de compartir",
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(text = "Compartir")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onError),
            onClick = onDeleteClick
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Icono de eliminar",
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(text = "Eliminar nota")
            }
        }
    }
}

@Composable
private fun ShowDeleteDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = MaterialTheme.colorScheme.background,
        title = { Text("Confirmar eliminación") },
        text = { Text("¿Estás seguro de que deseas eliminar esta nota?") },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = "Cancelar", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Sí, eliminar", color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
        }
    )
}