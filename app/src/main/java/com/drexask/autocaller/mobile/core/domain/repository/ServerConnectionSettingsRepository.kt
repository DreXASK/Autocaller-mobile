package com.drexask.autocaller.mobile.core.domain.repository

import com.drexask.autocaller.mobile.core.domain.models.ServerConnectionSettingsDto
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result

interface ServerConnectionSettingsRepository {
    suspend fun readSettings(): Result<ServerConnectionSettingsDto, ApiError.ServerConnectionSettingsError>
    suspend fun writeSettings(serverConnectionSettings: ServerConnectionSettingsDto): Result<Unit, ApiError>
    suspend fun deleteSettings(): Result<Unit, ApiError>
}