package com.moldedbits.githubsample

import android.app.Application
import timber.log.Timber

class GHApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}