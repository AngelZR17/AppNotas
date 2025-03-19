package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.kirodev.notasapp.TaskViewModel
import com.kirodev.notasapp.data.Tasks
import com.kirodev.notasapp.data.fechaHoraActual
import com.kirodev.notasapp.navigation.AppScreens
import java.nio.file.WatchEvent

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(tasks: List<Tasks>, taskViewModel: TaskViewModel, ctx: Context, navController: NavController) {
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Tasks?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showOptionsBottomSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text("Tareas", color = MaterialTheme.colorScheme.onSecondary) },
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
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = null
                                )
                            },
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
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Outlined.Help,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Outlined.OpenInNew,
                                    contentDescription = null
                                )
                            },
                            onClick = { /* Do something... */ }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreens.AddTask.route)
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
            if (tasks.isNotEmpty()) {
                LazyColumn {
                    items(tasks, key = { it.id!! }) { task ->
                        TaskCard(
                            task = task,
                            onTaskClick = {
                                taskViewModel.setSelectedTask(task)
                                navController.navigate(AppScreens.EditTask.route)
                            },
                            onTaskLongClick = {
                                selectedTask = task
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
                        imageVector = Icons.Filled.Task,
                        contentDescription = "Icono de tareas",
                        modifier = Modifier.padding(bottom = 15.dp).size(size = 40.dp)
                    )
                    Text(
                        text = "No hay tareas aún",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
    if (selectedTask != null && showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                onEditClick = {
                    selectedTask?.let { note ->
                        taskViewModel.setSelectedTask(note)
                        navController.navigate(AppScreens.EditTask.route)
                    }
                    showBottomSheet = false
                },
                onDeleteClick = {
                    showDeleteConfirmation = true
                    showBottomSheet = false
                }
            )
        }
    }
    if (showDeleteConfirmation) {
        ShowDeleteDialog(
            onConfirm = {
                selectedTask?.let { taskViewModel.deleteTask(it) }
                showDeleteConfirmation = false
            },
            onCancel = { showDeleteConfirmation = false }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskCard(
    task: Tasks,
    onTaskClick: () -> Unit,
    onTaskLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = onTaskClick,
                onLongClick = onTaskLongClick
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
                    text = task.task,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = task.dateUpdated,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                Text(text = "Editar tarea")
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
                Text(text = "Eliminar tarea")
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
        text = { Text("¿Estás seguro de que deseas eliminar esta tarea?") },
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