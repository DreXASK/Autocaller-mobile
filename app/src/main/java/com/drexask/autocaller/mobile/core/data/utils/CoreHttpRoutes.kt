package com.drexask.autocaller.mobile.core.data.utils

import com.drexask.autocaller.mobile.core.domain.ServerConnection
import org.koin.java.KoinJavaComponent.inject

object CoreHttpRoutes {

    private val serverConnection by inject<ServerConnection>(ServerConnection::class.java)

    val baseUrl
        get() = "http://${serverConnection.serverConnectionSettings?.ip}:${serverConnection.serverConnectionSettings?.port}"

    val getTokenStatus
        get() = "$baseUrl/check_token_status"

}