package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(notes: List<Notes>, notesViewModel: NotesViewModel, ctx: Context, navController: NavController) {
    var eliminarConfirmaciónrequerida by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
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
                                    .combinedClickable(
                                        onClick = {
                                            println("ajaaay perro")
                                        },
                                        onLongClick = {
                                            showBottomSheet = true
                                            println("ajaaay perro presionaleeee")
                                        }
                                    )
                            ) {
                                if (showBottomSheet) {
                                    ModalBottomSheet(
                                        modifier = Modifier,
                                        onDismissRequest = {
                                            showBottomSheet = false
                                        },
                                        sheetState = sheetState
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 15.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Button(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
                                                onClick = {  }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Create,
                                                        contentDescription = "Icono de editar",
                                                        modifier = Modifier.padding(end = 10.dp)
                                                    )
                                                    Text(text = "Editar nota")
                                                }
                                            }
                                            Button(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
                                                onClick = {  }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Star,
                                                        contentDescription = "Icono de favorito",
                                                        modifier = Modifier.padding(end = 10.dp)
                                                    )
                                                    Text(text = "Favorito")
                                                }
                                            }
                                            Button(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
                                                onClick = {  }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Share,
                                                        contentDescription = "Icono de compartir",
                                                        modifier = Modifier.padding(end = 10.dp)
                                                    )
                                                    Text(text = "Compartir")
                                                }
                                            }
                                            Button(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiary),
                                                onClick = { eliminarConfirmaciónrequerida = true }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Delete,
                                                        contentDescription = "Icono de editar",
                                                        modifier = Modifier.padding(end = 10.dp)
                                                    )
                                                    Text(text = "Eliminar nota")
                                                }
                                            }
                                            if (eliminarConfirmaciónrequerida) {
                                                mostrarDialogoEliminacion(
                                                    confirmarEliminacion = {
                                                        eliminarConfirmaciónrequerida = false
                                                        notesViewModel.deleteNotes(note)
                                                    },
                                                    cancelarEliminacion = {
                                                        eliminarConfirmaciónrequerida = false
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }
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
                    }
                }else{
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay ninguna nota agregada",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun mostrarDialogoEliminacion(
    confirmarEliminacion: () -> Unit,
    cancelarEliminacion: () -> Unit,
) {
    AlertDialog(onDismissRequest = {  },
        containerColor = MaterialTheme.colorScheme.background,
        title = { Text("Confirmar eliminación") },
        text = { Text("¿Estás seguro de que deseas eliminar esta nota?") },
        dismissButton = {
            TextButton(onClick = cancelarEliminacion) {
                Text(text = "Cancelar", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        },
        confirmButton = {
            TextButton(onClick = confirmarEliminacion) {
                Text(text = "Si, eliminar", color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
        }
    )
}