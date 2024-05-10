package com.drexask.autocaller.mobile.core.domain.repository.login

import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import com.drexask.autocaller.mobile.core.domain.utils.TokenStatus

interface LoginRepository {
    suspend fun getTokenStatus(parameter: LoginTypes.Parameter): Result<TokenStatus, ApiError>
}