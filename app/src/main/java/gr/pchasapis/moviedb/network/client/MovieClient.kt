package gr.pchasapis.moviedb.network.client

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.common.DefinitionsApi
import gr.pchasapis.moviedb.model.parsers.movie.MovieResponse
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.tv.TvShowResponse
import gr.pchasapis.moviedb.network.api.MovieApi
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MovieClient {

    private var movieApi: MovieApi

    init {
        movieApi = getRetrofit().create(MovieApi::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(DefinitionsApi.DOMAIN)
                .client(getOkHttpClient())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getOkHttpClient() = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()

    fun getSearchAsync(queryText: String, page: Int): Deferred<SearchResponse> {
        return movieApi.getSearchAsync(Definitions.API_KEY, queryText, page)
    }

    fun getMovieDetailsAsync(movieId: Int): Deferred<MovieResponse> {
        return movieApi.getMovieDetailsAsync(movieId, Definitions.API_KEY, Definitions.VIDEOS)
    }

    fun getTvShowDetailsAsync(tvId: Int): Deferred<TvShowResponse> {
        return movieApi.getTvShowDetailsAsync(tvId, Definitions.API_KEY, Definitions.VIDEOS)
    }

}