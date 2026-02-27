package gr.pchasapis.moviedb.common.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimberLogging()
    }

    private fun initTimberLogging() {
        Timber.plant(Timber.DebugTree())
    }
}
