package com.drexask.autocaller.mobile.caller.data.repository.callTasks

import com.drexask.autocaller.mobile.caller.data.utils.CallerHttpRoutes
import com.drexask.autocaller.mobile.caller.domain.models.CallTaskForDeviceDto
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksRepository
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksTypes
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class CallTasksRepositoryRemote(private val httpClient: HttpClient): CallTasksRepository {
    override suspend fun getCallTask(parameter: CallTasksTypes.Parameter.Get): Result<CallTaskForDeviceDto, ApiError.CallTasksError> {
        return try {
            val response = httpClient.post {
                url(CallerHttpRoutes.getCallTask)
                setBody(parameter as CallTasksParameterGetRemote)
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> Result.Success(response.body())
                else -> Result.Error(ApiError.CallTasksError.Remote.UnknownError(response.body()))
            }

        } catch (e: Exception) {
            Result.Error(ApiError.CallTasksError.Remote.UnknownError(e.message.toString()))
        }
    }

    override suspend fun sendResult(parameter: CallTasksTypes.Parameter.Send): Result<Unit, ApiError.CallTasksError> {
        return try {
            val response = httpClient.post {
                url(CallerHttpRoutes.sendResult)
                setBody(parameter as CallTasksParameterSendRemote)
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> Result.Success(response.body())
                else -> Result.Error(ApiError.CallTasksError.Remote.UnknownError(response.body()))
            }

        } catch (e: Exception) {
            Result.Error(ApiError.CallTasksError.Remote.UnknownError(e.message.toString()))
        }
    }


}