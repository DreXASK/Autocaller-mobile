package com.drexask.autocaller.mobile.connectionAdjuster.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.drexask.autocaller.mobile.connectionAdjuster.presentation.ConnectionAdjusterViewModel

const val DISCONNECTED_BUTTON_TAG = "DISCONNECTED_BUTTON_TAG"

@Composable
fun DisconnectedWindow(vm: ConnectionAdjusterViewModel) {

    var ipTextValue by remember { mutableStateOf("localhost") }
    var portTextValue by remember { mutableStateOf("8080") }
    var tokenTextValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            OutlinedTextField(
                value = ipTextValue,
                onValueChange = { ipTextValue = it },
                modifier = Modifier.width(250.dp),
                singleLine = true,
                label = { Text("IP") }
            )
            Spacer(Modifier.width(10.dp))
            OutlinedTextField(
                value = portTextValue,
                onValueChange = { portTextValue = it },
                modifier = Modifier.width(100.dp),
                singleLine = true,
                label = { Text("Port") }
            )

        }
        OutlinedTextField(
            value = tokenTextValue,
            onValueChange = { tokenTextValue = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Токен") }
        )
        OutlinedButton(
            onClick = {
                vm.connectToServer(ipTextValue, portTextValue, tokenTextValue)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Подключиться к серверу")
        }
    }
}
