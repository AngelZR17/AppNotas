package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text("Notas", color = MaterialTheme.colorScheme.onSecondary) },
                actions = {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Opciones",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Configuración") },
                            leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                            onClick = { navController.navigate(AppScreens.Settings.route) }
                        )
                        DropdownMenuItem(
                            text = { Text("Option 2") },
                            onClick = { /* Do something... */ }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("About") },
                            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                            onClick = { /* Do something... */ }
                        )
                        DropdownMenuItem(
                            text = { Text("Help") },
                            leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                            trailingIcon = { Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null) },
                            onClick = { /* Do something... */ }
                        )
                    }
                }
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
                                notesViewModel.setSelectedNote(note)
                                navController.navigate(AppScreens.EditNote.route)
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
                        contentDescription = "Icono de notas",
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
                        notesViewModel.setSelectedNote(note)
                        navController.navigate(AppScreens.EditNote.route)
                    }
                    showBottomSheet = false
                },
                onDeleteClick = {
                    showDeleteConfirmation = true
                    showBottomSheet = false
                },
                onShareClick = {
                    selectedNote?.let { note ->
                        shareContent(ctx, note.title, note.note)
                    }
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
                    text = note.note.ifEmpty { "Sin texto" } ?: "Sin texto",
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
            .padding(horizontal = 20.dp)
            .padding(bottom = 15.dp),
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
        icon = {
            Icon(
                Icons.Default.Warning,
                modifier = Modifier.size(35.dp),
                contentDescription = "Example Icon",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        },
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

private fun shareContent(ctx: Context, title: String, content: String,){
    val intent2 = Intent("android.intent.action.SEND").apply {
        type = "text/plain"
        putExtra("android.intent.extra.SUBJECT", "Notas")
        putExtra("android.intent.extra.TEXT", title + "\n\n" + content)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 268435456 en decimal es FLAG_ACTIVITY_NEW_TASK
    }
    ctx.startActivity(intent2)
}