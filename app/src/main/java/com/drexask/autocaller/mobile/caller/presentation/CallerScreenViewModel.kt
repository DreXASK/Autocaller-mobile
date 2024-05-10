package com.drexask.autocaller.mobile.caller.presentation

import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import com.drexask.autocaller.mobile.caller.domain.CallService
import kotlinx.coroutines.Job

class CallerScreenViewModel: ViewModel() {

    var abc: TextToSpeech? = null

    var job: Job? = null
    var callService: CallService? = null

}