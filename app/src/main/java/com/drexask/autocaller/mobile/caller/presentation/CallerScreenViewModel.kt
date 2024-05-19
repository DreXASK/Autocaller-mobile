package com.drexask.autocaller.mobile.caller.presentation

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drexask.autocaller.mobile.caller.data.repository.callTasks.CallTasksParameterGetRemote
import com.drexask.autocaller.mobile.caller.data.repository.callTasks.CallTasksParameterSendRemote
import com.drexask.autocaller.mobile.caller.domain.InformService
import com.drexask.autocaller.mobile.caller.domain.usecase.GetCallTaskUseCase
import com.drexask.autocaller.mobile.caller.domain.usecase.SendResultUseCase
import com.drexask.autocaller.mobile.core.domain.ServerConnection
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.DeleteServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class CallerScreenViewModel : ViewModel() {

    private val getCallTaskUseCase by inject<GetCallTaskUseCase>(GetCallTaskUseCase::class.java)
    private val deleteServerConnectionSettingsUseCase by inject<DeleteServerConnectionSettingsUseCase>(
        DeleteServerConnectionSettingsUseCase::class.java
    )
    private val sendResultUseCase by inject<SendResultUseCase>(SendResultUseCase::class.java)
    val serverConnection by inject<ServerConnection>(ServerConnection::class.java)

    var textToSpeech: TextToSpeech? = null
    var job = mutableStateOf<Job?>(null)
    var informService: InformService? = null

    fun launchCallingProcess() {
        job.value = CoroutineScope(Dispatchers.Default).launch {
            while (true) {

                val token = serverConnection.serverConnectionSettings?.token
                    ?: throw Exception("ServerConnectionSettings is null")

                val getResult = getCallTaskUseCase.execute(CallTasksParameterGetRemote(token))
                if (getResult is Result.Error) {
                    Log.d("calls", "Не удалось получить задание, ожидание 10 секунд")
                    delay(10 * 1000L /* 10 sec */)
                    continue
                }
                getResult as Result.Success

                textToSpeech?.let {
                    val callResult = informService?.makeCall(
                        textToSpeechService = it,
                        phoneNumber = getResult.data.phoneNumber,
                        messageToReadAloud = getResult.data.messageText
                    )

                    val isSuccess: Boolean

                    if (callResult == true) {
                        isSuccess = true
                        Log.d("calls", "Статус звонка - успешно")
                    } else {
                        if (getResult.data.callAttempts == 2) {
                            Log.d("calls", "Количество попыток дозвона 2 - отправляю SMS")
                            informService?.sendSms(
                                phoneNumber = getResult.data.phoneNumber,
                                messageToSend = getResult.data.messageText
                            )
                        }
                        isSuccess = false
                        Log.d("calls", "Статус звонка - неудача")
                    }

                    while (true) {
                        val sendResult = sendResultUseCase.execute(
                            CallTasksParameterSendRemote(
                                token = token,
                                callTaskId = getResult.data.id,
                                isSuccess = isSuccess
                            )
                        )

                        when (sendResult) {
                            is Result.Success -> {
                                Log.d("calls", "Результат успешно отправлен")
                                break
                            }

                            is Result.Error -> {
                                when (sendResult.error) {
                                    is ApiError.CallTasksError.Remote.ConnectionRefused -> {
                                        Log.d(
                                            "calls",
                                            "Результат не отправлен - connection refused"
                                        )
                                    }

                                    is ApiError.CallTasksError.Remote.UnknownError -> {
                                        throw Exception("sendResult error - ${sendResult.error.text}")
                                    }
                                }
                            }
                        }
                    }


                }


            }

        }
    }

    fun logOutFromServer() {
        viewModelScope.launch {
            serverConnection.logoutFromServer()
            deleteServerConnectionSettingsUseCase.execute()
        }
    }

}