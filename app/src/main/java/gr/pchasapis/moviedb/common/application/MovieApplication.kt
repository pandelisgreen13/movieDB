package gr.pchasapis.moviedb.common.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import gr.pchasapis.moviedb.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimberLogging()
    }

    private fun initTimberLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}