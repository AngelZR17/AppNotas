package com.kirodev.notasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kirodev.notasapp.navigation.Navigation
import com.kirodev.notasapp.ui.theme.NotasAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotasAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val ctx = applicationContext
                    Navigation(ctx)
                }
            }
        }
    }
}
