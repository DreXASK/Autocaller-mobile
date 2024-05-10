package com.drexask.autocaller.mobile.connectionAdjuster.presentation

import androidx.lifecycle.ViewModel
import com.drexask.autocaller.mobile.core.domain.ServerConnection
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.DeleteServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.SaveServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.core.domain.utils.ServerConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject
import com.drexask.autocaller.mobile.core.domain.utils.Result
import org.koin.java.KoinJavaComponent.get

class ConnectionAdjusterViewModel: ViewModel() {

    val serverConnection by inject<ServerConnection>(ServerConnection::class.java)

    private var connectionJob: Job? = null

    fun connectToServer(ip: String, port: String, token: String) {
        connectionJob = CoroutineScope(Dispatchers.Default).launch {
            val saveServerConnectionSettingsUseCase = get<SaveServerConnectionSettingsUseCase>(
                    SaveServerConnectionSettingsUseCase::class.java
                )

            val result = serverConnection.loginOnServer(ip, port, token)

            if (result is Result.Success) {
                serverConnection.serverConnectionSettings?.let {
                    saveServerConnectionSettingsUseCase.execute(it)
                    println("ServerConnectionSettings saved to the file storage")
                }
            }
        }
    }

    fun abortConnectionProcess() {
        connectionJob?.cancel()
        serverConnection.connectionStatus.value = ServerConnectionStatus.DISCONNECTED
    }

    fun logOutFromServer() {
        CoroutineScope(Dispatchers.Default).launch {
            val deleteServerConnectionSettingsUseCase =
                KoinJavaComponent.get<DeleteServerConnectionSettingsUseCase>(
                    DeleteServerConnectionSettingsUseCase::class.java
                )

            serverConnection.logoutFromServer()
            if (deleteServerConnectionSettingsUseCase.execute() is Result.Success)
                println("ServerConnectionSettings removed from the file storage")
        }
    }


}