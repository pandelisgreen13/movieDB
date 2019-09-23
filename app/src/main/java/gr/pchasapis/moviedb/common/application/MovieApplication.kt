package gr.pchasapis.moviedb.common.application

import android.app.Application
import gr.pchasapis.moviedb.BuildConfig
import gr.pchasapis.moviedb.network.client.MovieClient
import timber.log.Timber
import java.lang.ref.WeakReference


class MovieApplication : Application() {

    companion object {
        private var instance: WeakReference<MovieApplication>? = null

        @JvmStatic
        fun get(): MovieApplication? {
            return instance?.get()
        }
    }

    val movieClient: MovieClient by lazy {
        return@lazy MovieClient()
    }

    override fun onCreate() {
        super.onCreate()
        instance = WeakReference(this)
        initTimberLogging()
    }

    private fun initTimberLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}