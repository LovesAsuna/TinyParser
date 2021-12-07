package com.hyosakura.parser

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hyosakura.parser.window.MainWindowState

@Composable
fun rememberApplicationState(applicationExit: () -> Unit) = remember {
    ApplicationState {
        applicationExit()
    }
}


class ApplicationState(exit: MainWindowState.() -> Unit) {
    val mainWindowState = MainWindowState(exit)
}

