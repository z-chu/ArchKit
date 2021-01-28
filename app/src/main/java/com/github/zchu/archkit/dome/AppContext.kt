package com.github.zchu.archkit.dome

import android.app.Application
import com.github.zchu.arch.koin.auto.service.installAutoRegister
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AppContext :Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            installAutoRegister()
        }

    }
}