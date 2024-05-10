package com.drexask.autocaller.mobile.connectionAdjuster.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.drexask.autocaller.mobile.connectionAdjuster.presentation.components.ConnectingWindow
import com.drexask.autocaller.mobile.connectionAdjuster.presentation.components.DisconnectedWindow
import com.drexask.autocaller.mobile.core.domain.utils.ServerConnectionStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConnectionAdjusterScreen() {
    val vm = koinViewModel<ConnectionAdjusterViewModel>()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (vm.serverConnection.connectionStatus.value) {
            ServerConnectionStatus.DISCONNECTED ->
                DisconnectedWindow(vm)
            ServerConnectionStatus.CONNECTING ->
                ConnectingWindow(vm)
            ServerConnectionStatus.CONNECTED ->
                Text("Подключение успешно")
        }
    }


}