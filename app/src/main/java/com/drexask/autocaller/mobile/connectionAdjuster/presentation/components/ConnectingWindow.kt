package com.drexask.autocaller.mobile.connectionAdjuster.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drexask.autocaller.mobile.connectionAdjuster.presentation.ConnectionAdjusterViewModel
import com.drexask.autocaller.mobile.core.presentation.components.OutlinedButtonWithProgress


@Composable
fun ConnectingWindow(vm: ConnectionAdjusterViewModel) {

    OutlinedButtonWithProgress(
        onClick = {
            vm.abortConnectionProcess()
        },
        buttonText = { Text("Отмена подключения") }
    )
}