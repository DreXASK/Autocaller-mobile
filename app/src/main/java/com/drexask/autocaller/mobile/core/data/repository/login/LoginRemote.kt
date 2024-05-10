package com.drexask.autocaller.mobile.core.data.repository.login

import com.drexask.autocaller.mobile.core.domain.utils.TokenStatus
import com.drexask.autocaller.mobile.core.domain.repository.login.LoginTypes
import kotlinx.serialization.Serializable

@Serializable
data class LoginParameterRemote(
	val token: String
): LoginTypes.Parameter

@Serializable
data class LoginResponseRemote(
	val tokenStatus: TokenStatus
): LoginTypes.Response
