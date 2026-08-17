package com.okmyan.composeuiplayground

import android.app.Application
import com.okmyan.composeuiplayground.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

class ComposeUiPlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        GlobalContext.startKoin {
            androidContext(this@ComposeUiPlaygroundApplication)
            androidLogger()
            modules(appModule)
        }
    }
}
