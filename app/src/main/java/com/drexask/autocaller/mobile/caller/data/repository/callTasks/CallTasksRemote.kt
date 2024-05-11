package com.drexask.autocaller.mobile.caller.data.repository.callTasks

import com.drexask.autocaller.mobile.caller.domain.models.CallTaskForDeviceDto
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksTypes
import kotlinx.serialization.Serializable

@Serializable
data class CallTasksParameterSendRemote (
    val token: String,
    val callTaskId: Long,
    val isSuccess: Boolean
): CallTasksTypes.Parameter.Send

@Serializable
data class CallTasksParameterGetRemote(
    val token: String
): CallTasksTypes.Parameter.Get