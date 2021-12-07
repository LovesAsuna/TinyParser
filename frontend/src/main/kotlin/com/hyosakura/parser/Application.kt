package com.hyosakura.parser

import androidx.compose.runtime.Composable
import com.hyosakura.parser.window.MainWindow

@Composable
fun Application(state: ApplicationState) {
    MainWindow(state.mainWindowState)
}