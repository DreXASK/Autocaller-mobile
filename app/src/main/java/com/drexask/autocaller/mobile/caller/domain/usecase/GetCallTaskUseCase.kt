package com.drexask.autocaller.mobile.caller.domain.usecase

import com.drexask.autocaller.mobile.caller.domain.models.CallTaskForDeviceDto
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksRepository
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksTypes
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result

class GetCallTaskUseCase(private val repository: CallTasksRepository) {

    suspend fun execute(parameter: CallTasksTypes.Parameter.Get): Result<CallTaskForDeviceDto, ApiError.CallTasksError> {
        return repository.getCallTask(parameter)
    }

}