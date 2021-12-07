package com.hyosakura.parser.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.hyosakura.tinyparser.Parser
import kotlinx.coroutines.launch

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MainWindow(state: MainWindowState) {
    val scope = rememberCoroutineScope()

    fun exit() = scope.launch { state.exit() }

    Window(
        state = state.window,
        title = "TinyParser",
        resizable = false,
        onCloseRequest = { exit() }
    ) {
        Column(modifier = Modifier.background(Color(222, 222, 222))) {
            Row(modifier = Modifier.weight(0.25F).offset(y = 5.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxSize().background(Color(240, 240, 240)).weight(1F),
                    label = {
                        Text(
                            text = "Code",
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                        )
                    },
                    placeholder = {
                        Text("代码")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = TextUnit(15F, TextUnitType.Sp)),
                    value = state.code,
                    onValueChange = {
                        state.code = it
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxSize().background(Color(240, 240, 240)).weight(1F),
                    label = {
                        Text(
                            text = "SyntaxTree",
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                        )
                    },
                    placeholder = {
                        Text("语法树")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = TextUnit(15F, TextUnitType.Sp)),
                    value = state.tree,
                    onValueChange = {
                        state.tree = it
                    }
                )
            }
            Row(
                modifier = Modifier.padding(5.dp).background(Color(200, 200, 200)).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    clear(state)
                }) {
                    Text("清空")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    parse(state)
                }) {
                    Text("生成语法树")
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

fun clear(state: MainWindowState) {
    state.code = ""
    state.tree = ""
}

fun parse(state: MainWindowState) {
    val parser = Parser()
    runCatching {
        parser.parseToTree(state.code)
    }.onSuccess {
        state.tree = it.toString()
    }.onFailure {
        state.tree = it.message ?: "未知错误"
    }
}