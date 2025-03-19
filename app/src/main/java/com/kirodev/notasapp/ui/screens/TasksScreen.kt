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
                    showBottomSheet = true
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
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .size(size = 40.dp)
                    )
                    Text(
                        text = "No hay tareas aún",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                onAddClick = { title ->
                    taskViewModel.createTask(title) // Pass the title to the ViewModel
                    showBottomSheet = false // Dismiss the bottom sheet
                },
            )
        }
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
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    onAddClick: (String) -> Unit
) {
    val currentTitle = rememberSaveable { mutableStateOf("") }
    Row {
        TextField(
            modifier = Modifier.padding(horizontal = 10.dp).width(300.dp),
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
        Button(
            modifier = Modifier.width(90.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
            onClick = {
                onAddClick(currentTitle.value) // Pass the title to the ViewModel
                currentTitle.value = "" // Clear the input field
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Icono de editar",
                modifier = Modifier
            )
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

private fun shareContent(ctx: Context, title: String, content: String,){
    val intent2 = Intent("android.intent.action.SEND").apply {
        type = "text/plain"
        putExtra("android.intent.extra.SUBJECT", "Notas")
        putExtra("android.intent.extra.TEXT", title + "\n\n" + content)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 268435456 en decimal es FLAG_ACTIVITY_NEW_TASK
    }
    ctx.startActivity(intent2)
}