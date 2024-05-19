package com.drexask.autocaller.mobile.caller.presentation

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.drexask.autocaller.mobile.caller.domain.InformService
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun CallerScreen() {
    val vm = koinViewModel<CallerScreenViewModel>()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.informService = InformService(context)

        vm.textToSpeech = TextToSpeech(context) {
            if (it != TextToSpeech.ERROR) {
                Log.e("calls", "Text to speech initialization error")
            }
        }.apply {
            println(isLanguageAvailable(Locale("ru")))
            language = Locale("ru")
        }
    }

    Box {
        OutlinedButton(
            onClick = {
                if (vm.job.value == null) {
                    vm.launchCallingProcess()
                } else {
                    vm.job.value?.cancel()
                    vm.job.value = null
                }
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = if (vm.job.value == null) "Начать процесс обзвона" else "Стоп!")
        }
        Card(Modifier
            .width(IntrinsicSize.Max)
            .align(Alignment.TopCenter)
            .padding(50.dp)) {
            Column {
                Text(
                    text = "Подключено\n${vm.serverConnection.serverConnectionSettings?.ip}" +
                        ":${vm.serverConnection.serverConnectionSettings?.port}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
                OutlinedButton(
                    onClick = vm::logOutFromServer,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Отключиться от сервера")
                }
            }
        }
    }

}