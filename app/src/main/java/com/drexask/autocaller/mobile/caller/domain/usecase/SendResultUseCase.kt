package com.drexask.autocaller.mobile.caller.domain.usecase

import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksRepository
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksTypes
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result

class SendResultUseCase(private val repository: CallTasksRepository) {

    suspend fun execute(parameter: CallTasksTypes.Parameter.Send): Result<Unit, ApiError.CallTasksError> {
        return repository.sendResult(parameter)
    }

}