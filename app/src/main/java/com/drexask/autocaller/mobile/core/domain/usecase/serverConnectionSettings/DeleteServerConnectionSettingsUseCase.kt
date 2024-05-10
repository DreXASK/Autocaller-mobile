package com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings

import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import com.drexask.autocaller.mobile.core.domain.repository.ServerConnectionSettingsRepository

class DeleteServerConnectionSettingsUseCase(private val repository: ServerConnectionSettingsRepository) {

    suspend fun execute(): Result<Unit, ApiError> {
        return repository.deleteSettings()
    }

}