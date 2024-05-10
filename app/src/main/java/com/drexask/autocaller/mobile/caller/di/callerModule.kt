package com.drexask.autocaller.mobile.caller.di

import com.drexask.autocaller.mobile.caller.presentation.CallerScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val callerModule = module {
    viewModel {
        CallerScreenViewModel()
    }
}