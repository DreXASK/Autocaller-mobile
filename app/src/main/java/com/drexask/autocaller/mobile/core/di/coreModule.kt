package com.drexask.autocaller.mobile.core.di

import com.drexask.autocaller.mobile.core.data.repository.ServerConnectionSettingsRepositoryLocal
import com.drexask.autocaller.mobile.core.data.repository.login.LoginRepositoryRemote
import com.drexask.autocaller.mobile.core.domain.ServerConnection
import com.drexask.autocaller.mobile.core.domain.repository.login.LoginRepository
import com.drexask.autocaller.mobile.core.domain.usecase.LoginOnServerUseCase
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.DeleteServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.GetServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.SaveServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.core.domain.repository.ServerConnectionSettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single {
        ServerConnection()
    }
    single {
        HttpClient(CIO) {
            //    install(Logging) { level = LogLevel.ALL }
            install(ContentNegotiation) {
                json()
            }
        }
    }
    single<LoginRepository> {
        LoginRepositoryRemote(httpClient = get())
    }
    single {
        LoginOnServerUseCase(repository = get())
    }
    single {
        GetServerConnectionSettingsUseCase(repository = get())
    }
    single {
        SaveServerConnectionSettingsUseCase(repository = get())
    }
    single {
        DeleteServerConnectionSettingsUseCase(repository = get())
    }
    single<ServerConnectionSettingsRepository> {
        ServerConnectionSettingsRepositoryLocal(context = androidContext())
    }
}