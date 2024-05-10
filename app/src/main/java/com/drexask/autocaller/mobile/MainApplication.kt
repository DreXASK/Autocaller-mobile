package com.drexask.autocaller.mobile

import android.app.Application
import com.drexask.autocaller.mobile.caller.di.callerModule
import com.drexask.autocaller.mobile.connectionAdjuster.di.connectingAdjusterModule
import com.drexask.autocaller.mobile.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(androidContext = applicationContext)
            modules(
                connectingAdjusterModule,
                coreModule,
                callerModule
            )
        }
    }

}