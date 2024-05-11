package com.drexask.autocaller.mobile.caller.presentation

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
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

    Box(
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = { if (vm.job.value == null) {
                vm.launchCallingProcess()
            } else {
                vm.job.value?.cancel()
                vm.job.value = null
            }}
        ) {
            Text(text = if (vm.job.value == null) "Пуск!" else "Стоп!")
        }
    }
}