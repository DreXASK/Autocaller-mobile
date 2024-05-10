package com.drexask.autocaller.mobile.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerConnectionSettingsDto(
    val ip: String,
    val port: String,
    val token: String
)