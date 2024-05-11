package com.drexask.autocaller.mobile.caller.di

import com.drexask.autocaller.mobile.caller.data.repository.callTasks.CallTasksRepositoryRemote
import com.drexask.autocaller.mobile.caller.domain.repository.callTasks.CallTasksRepository
import com.drexask.autocaller.mobile.caller.domain.usecase.GetCallTaskUseCase
import com.drexask.autocaller.mobile.caller.domain.usecase.SendResultUseCase
import com.drexask.autocaller.mobile.caller.presentation.CallerScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val callerModule = module {
    viewModel {
        CallerScreenViewModel()
    }
    single<CallTasksRepository> {
        CallTasksRepositoryRemote(httpClient = get())
    }
    single {
        GetCallTaskUseCase(repository = get())
    }
    single {
        SendResultUseCase(repository = get())
    }
}