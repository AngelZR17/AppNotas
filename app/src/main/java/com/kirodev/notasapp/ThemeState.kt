package com.kirodev.notasapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf

val LocalDarkMode = staticCompositionLocalOf { mutableStateOf(false) }