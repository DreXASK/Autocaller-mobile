package com.drexask.autocaller.mobile.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.drexask.autocaller.mobile.core.domain.models.ServerConnectionSettingsDto
import com.drexask.autocaller.mobile.core.domain.utils.ApiError
import com.drexask.autocaller.mobile.core.domain.utils.Result
import com.drexask.autocaller.mobile.core.data.utils.SETTINGS_FILE_NAME
import com.drexask.autocaller.mobile.core.domain.repository.ServerConnectionSettingsRepository
import com.drexask.autocaller.mobile.core.utils.EncryptionDecryptionUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class ServerConnectionSettingsRepositoryLocal(private val context: Context): ServerConnectionSettingsRepository {

    private val sharedPreferencesName = "ServerConnectionSettings"
    private val sharedPreferencesDataKey = "Data"

    override suspend fun readSettings(): Result<ServerConnectionSettingsDto, ApiError.ServerConnectionSettingsError> {
        val encryptedText: String

        try {
            val settings = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
            encryptedText = settings.getString(sharedPreferencesDataKey, null) ?: throw FileNotFoundException()
        } catch (e: FileNotFoundException) {
            return Result.Error(ApiError.ServerConnectionSettingsError.FILE_DOES_NOT_EXIST)
        }

        val decryptedJson = EncryptionDecryptionUtil.decrypt("secret", encryptedText)
        val result = Json.decodeFromString<ServerConnectionSettingsDto>(decryptedJson)

        return Result.Success(result)
    }

    override suspend fun writeSettings(serverConnectionSettings: ServerConnectionSettingsDto): Result<Unit, ApiError> {
        val dataJson = Json.encodeToString(serverConnectionSettings)
        val encryptedText = EncryptionDecryptionUtil.encrypt("secret", dataJson)

        try {
            val settings = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putString(sharedPreferencesDataKey, encryptedText)
            editor.apply()
        } catch (e: Exception) {
            return Result.Error(ApiError.UnknownError(e.message.toString()))
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteSettings(): Result<Unit, ApiError> {
        try {
            context.deleteSharedPreferences(sharedPreferencesName)
        } catch (e: Exception) {
            return Result.Error(ApiError.UnknownError(e.message.toString()))
        }
        return Result.Success(Unit)
    }

}
