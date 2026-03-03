package gr.pchasapis.moviedb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.pchasapis.moviedb.common.DefinitionsApi
import gr.pchasapis.moviedb.network.api.MovieApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        val json = Json {
            ignoreUnknownKeys = true // Prevents crashes if API adds new fields
            coerceInputValues = true // Uses default values if null is received
        }
        return Retrofit.Builder()
            .baseUrl(DefinitionsApi.DOMAIN)
            .client(getOkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .build()
            .create(MovieApi::class.java)
    }

    private fun getOkHttpClient() = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .build()
}
