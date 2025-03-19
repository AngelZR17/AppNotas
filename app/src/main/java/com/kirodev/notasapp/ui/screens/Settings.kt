package com.kirodev.notasapp.ui.screens

import android.R.attr.checked
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kirodev.notasapp.NotesViewModel
import com.kirodev.notasapp.util.Preferences

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(ctx: Context, navController: NavController) {
    var showRestartConfirmation by rememberSaveable { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(Preferences(ctx).obtenerBoolean("dark", false)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                        title = { Text("Configuración", color = MaterialTheme.colorScheme.onSecondary) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Botón de Retroceso",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        Column {
                            SwitchWithLabel(
                                label = "Modo Oscuro",
                                state = isDarkMode,
                                onStateChange = { newState ->
                                    isDarkMode = newState
                                    Preferences(ctx).guardarBoolean("dark", newState)
                                    showRestartConfirmation = true
                                }
                            )

                        }
                        if (showRestartConfirmation) {
                            ShowRestartDialog(
                                onConfirm = {
                                    restartApp(ctx)
                                    showRestartConfirmation = false
                                },
                                onCancel = { showRestartConfirmation = false }
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun SwitchWithLabel(label: String, state: Boolean, onStateChange: (Boolean) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Switch,
                onClick = { onStateChange(!state) }
            )
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Switch(
            modifier = Modifier,
            checked = state,
            onCheckedChange = { onStateChange(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onSecondary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            thumbContent = if (state) {
                {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                        tint = Color.Black
                    )
                }
            } else {
                null
            }
        )
    }
}

@Composable
private fun ShowRestartDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = MaterialTheme.colorScheme.background,
        title = { Text("Reiniciar aplicación") },
        text = { Text("¿Deseas reiniciar la aplicación para aplicar los cambios?") },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = "Cancelar", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Reiniciar", color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
        }
    )
}

private fun restartApp(ctx: Context) {
    val intent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)

    if (intent != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(intent)

        if (ctx is Activity) {
            ctx.finish()
        }

        android.os.Process.killProcess(android.os.Process.myPid())
    } else {
        Log.e("RestartApp", "No se pudo obtener el intent de lanzamiento para el paquete: ${ctx.packageName}")
    }
}

//@Composable
//fun DrawingScreen() {
//    val lines = remember {
//        mutableStateListOf<Line>()
//    }
//
//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//            .pointerInput(true) {
//                detectDragGestures { change, dragAmount ->
//                    change.consume()
//
//                    val line = Line(
//                        start = change.position - dragAmount,
//                        end = change.position
//                    )
//
//                    lines.add(line)
//                }
//            }
//    ) {
//        lines.forEach { line ->
//            drawLine(
//                color = line.color,
//                start = line.start,
//                end = line.end,
//                strokeWidth = line.strokeWidth.toPx(),
//                cap = StrokeCap.Round
//            )
//        }
//    }
//}
//
//data class Line(
//    val start: Offset,
//    val end: Offset,
//    val color: Color = Color.White,
//    val strokeWidth: Dp = 1.dp
//)