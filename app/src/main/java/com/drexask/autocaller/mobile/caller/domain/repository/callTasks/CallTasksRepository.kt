package com.drexask.autocaller.mobile.caller.domain.repository.callTasks

import com.drexask.autocaller.mobile.caller.domain.models.CallTaskForDeviceDto
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result

interface CallTasksRepository {
    suspend fun getCallTask(parameter: CallTasksTypes.Parameter.Get): Result<CallTaskForDeviceDto, ApiError.CallTasksError>

    suspend fun sendResult(parameter: CallTasksTypes.Parameter.Send): Result<Unit, ApiError.CallTasksError>

}