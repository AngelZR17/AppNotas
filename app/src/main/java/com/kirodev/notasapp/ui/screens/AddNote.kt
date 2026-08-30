package com.kirodev.notasapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kirodev.notasapp.NotesViewModel
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kirodev.notasapp.navigation.AppScreens


import java.io.File
import java.net.URI

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(notesViewModel: NotesViewModel, ctx: Context, navController: NavController){
    var selectedImage by remember { mutableStateOf<String?>(null) }
    val currentTitle = rememberSaveable { mutableStateOf("") }
    val currentDescription = rememberSaveable { mutableStateOf("") }
    var urisPhotos by remember { mutableStateOf(listOf<String>()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            // Otorgar permiso de lectura permanente
            ctx.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            urisPhotos = urisPhotos.plus(uri.toString() + "|IMG")
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                        title = { Text("Agregar Nota", color = MaterialTheme.colorScheme.onSecondary) },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Botón de Retroceso", tint = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = { getImageRequest.launch(arrayOf("image/*")) }) {
                                Icon(Icons.Filled.AttachFile, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Camera, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Mic, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Videocam, contentDescription = "Localized description")
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(Icons.Filled.Palette, contentDescription = "Localized description")
                            }
                        },
                        Modifier.windowInsetsPadding(WindowInsets.ime),
                        floatingActionButton = {
                            if(currentTitle.value.isNotBlank()){
                                FloatingActionButton(
                                    onClick = {
                                        notesViewModel.createNote(
                                            currentTitle.value,
                                            currentDescription.value,
                                            urisPhotos.joinToString()
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
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
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

                            Spacer(modifier = Modifier.height(3.dp))
                            LazyVerticalGrid(
                                modifier = Modifier.padding(top = 0.dp, start = 0.dp),
                                contentPadding = PaddingValues(0.dp),
                                columns = GridCells.Adaptive(minSize = 128.dp)
                            ){
                                itemsIndexed(urisPhotos){index, uri ->
                                    tarjetaMedia(
                                        uri = uri,
                                        onImageClick = {
                                            selectedImage = uri
                                        },
                                        onImageLongClick = {
                                            showBottomSheet = true
                                        }
                                    )
                                }
                            }
                            if (selectedImage != null) {
                                val imageUri = Uri.parse(selectedImage!!.split("|")[0])
                                Dialog(
                                    onDismissRequest = { selectedImage = null },
                                    properties = DialogProperties(usePlatformDefaultWidth = false)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Transparent)
                                            .clickable { selectedImage = null }
                                    ) {
                                        AsyncImage(
                                            model = imageUri,
                                            contentDescription = "Full screen image",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                    }
                                }
                            }
                            if (showBottomSheet) {
                                ModalBottomSheet(
                                    onDismissRequest = { showBottomSheet = false },
                                    sheetState = sheetState
                                ) {
                                    BottomSheetContent(
                                        onEditClick = {
                                            showBottomSheet = false
                                        },
                                        onDeleteClick = {
                                            showBottomSheet = false
                                        },
                                        onShareClick = {
                                            showBottomSheet = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun tarjetaMedia(
    uri: String,
    onImageClick: () -> Unit,
    onImageLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .sizeIn(minHeight = 72.dp)
    ) {
        Column(modifier = Modifier.weight(5f)) {
            var arreglo = uri.split("|")
            if (arreglo.get(1).equals("IMG")) {
                AsyncImage(
                    model = Uri.parse(arreglo.get(0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp)
                        .combinedClickable(
                            onClick = onImageClick,
                            onLongClick = onImageLongClick
                        ),
                    contentDescription = "Selected image",
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
