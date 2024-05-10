package com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings

import com.drexask.autocaller.mobile.core.domain.models.ServerConnectionSettingsDto
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import com.drexask.autocaller.mobile.core.domain.repository.ServerConnectionSettingsRepository

class GetServerConnectionSettingsUseCase(private val repository: ServerConnectionSettingsRepository) {

    suspend fun execute(): Result<ServerConnectionSettingsDto, ApiError.ServerConnectionSettingsError> {
        return repository.readSettings()
    }

}