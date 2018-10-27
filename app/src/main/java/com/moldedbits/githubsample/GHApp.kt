package com.moldedbits.githubsample

import android.app.Application
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import com.moldedbits.githubsample.di.appModule
import com.moldedbits.githubsample.di.githubApp
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class GHApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin(this, githubApp)

        Iconify.with(FontAwesomeModule())
    }
}