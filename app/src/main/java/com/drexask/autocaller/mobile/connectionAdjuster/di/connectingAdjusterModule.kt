package com.drexask.autocaller.mobile.connectionAdjuster.di

import com.drexask.autocaller.mobile.connectionAdjuster.presentation.ConnectionAdjusterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val connectingAdjusterModule = module {
    viewModel {
        ConnectionAdjusterViewModel()
    }
}