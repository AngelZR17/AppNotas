package com.kirodev.notasapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kirodev.notasapp.app.NotesApplication
import com.kirodev.notasapp.navigation.Navigation
import com.kirodev.notasapp.ui.theme.NotasAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var notesViewModel : NotesViewModel
    private lateinit var taskViewModel : TaskViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notesViewModel =  NotesViewModelFactory(NotesApplication.getDao()).create(NotesViewModel::class.java)
        taskViewModel =  TaskViewModelFactory(NotesApplication.getDaoTask()).create(TaskViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            NotasAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val ctx = applicationContext
                    Navigation(taskViewModel,notesViewModel,ctx)
                }
            }
        }
    }
}
