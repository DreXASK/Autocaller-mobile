package com.drexask.autocaller.mobile.core.data.utils

import com.drexask.autocaller.mobile.core.domain.ServerConnection
import org.koin.java.KoinJavaComponent.inject

object CoreHttpRoutes {

    private val serverConnection by inject<ServerConnection>(ServerConnection::class.java)

    private val baseUrl
        get() = "http://${serverConnection.serverConnectionSettings?.ip}:${serverConnection.serverConnectionSettings?.port}"

    val getTokenStatus
        get() = "$baseUrl/check_token_status"

    val getCallTasks
        get() = "$baseUrl/get_tasks_from_server"
    val sendCallTasks
        get() = "$baseUrl/send_tasks_to_server"
    val removeCallTasks
        get() = "$baseUrl/remove_task_from_server"

    val getMessageTemplates
        get() = "$baseUrl/get_message_templates_from_server"
    val removeMessageTemplate
        get() = "$baseUrl/remove_message_template_from_server"
    val sendMessageTemplate
        get() = "$baseUrl/send_message_template_to_server"
}