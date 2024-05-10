package com.drexask.autocaller.mobile.core.domain.usecase

import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import com.drexask.autocaller.mobile.core.data.repository.login.LoginParameterRemote
import com.drexask.autocaller.mobile.core.domain.utils.TokenStatus
import com.drexask.autocaller.mobile.core.domain.repository.login.LoginRepository

class LoginOnServerUseCase(private val repository: LoginRepository) {

	suspend fun execute(token: LoginParameterRemote): Result<TokenStatus, ApiError> {
		val result = repository.getTokenStatus(token)
		return result
	}

}