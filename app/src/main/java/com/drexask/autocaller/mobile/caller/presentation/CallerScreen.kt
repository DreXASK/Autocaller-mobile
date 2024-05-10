package com.drexask.autocaller.mobile.caller.presentation

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.drexask.autocaller.mobile.caller.domain.CallService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun CallerScreen() {

    val vm = koinViewModel<CallerScreenViewModel>()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.callService = CallService(context)

        vm.abc = TextToSpeech(context) {
            if (it != TextToSpeech.ERROR) {
                Log.e("calls", "Text to speech initialization error")
            }
        }.apply {
            println(isLanguageAvailable(Locale("ru")))
            language = Locale("ru")
        }
    }

    OutlinedButton(
        onClick = {

            //smsManager.sendTextMessage("89020285716", null, "sms message", null, null)
            vm.job?.cancel()
            vm.job = CoroutineScope(Dispatchers.Default).launch {

                vm.abc?.let {
                    val result = vm.callService?.makeCall(
                        abc = it
                    )
                    if (result == true) {
                        Log.d("calls", "success!!!!!!!!!!!!!!!!!!!")
                    }
                }

            }

        }
    ) {
        Text("Кнопка!")
    }
}