package com.hyosakura.analyzer.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState

class MainWindowState(
    private val exit: MainWindowState.() -> Unit
) {
    var code by mutableStateOf("")

    var tree by mutableStateOf("")

    val window = WindowState(height = 800.dp)

    fun exit() = exit(this)
}
