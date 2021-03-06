package gr.pchasapis.moviedb.network.client

import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.common.DefinitionsApi
import gr.pchasapis.moviedb.model.parsers.movie.MovieResponse
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.theatre.TheatreResponse
import gr.pchasapis.moviedb.model.parsers.tv.TvShowResponse
import gr.pchasapis.moviedb.network.api.MovieApi
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
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getOkHttpClient() = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()

    suspend fun getSearchAsync(queryText: String, page: Int): SearchResponse {
        return movieApi.getSearchAsync(Definitions.API_KEY, queryText, page)
    }

    suspend fun getMovieDetailsAsync(movieId: Int): MovieResponse {
        return movieApi.getMovieDetailsAsync(movieId, Definitions.API_KEY, Definitions.VIDEOS)
    }

    suspend fun getTvShowDetailsAsync(tvId: Int): TvShowResponse {
        return movieApi.getTvShowDetailsAsync(tvId, Definitions.API_KEY, Definitions.VIDEOS)
    }

    suspend fun getMovieTheatre(startDate: String, endDate: String): TheatreResponse {
        return movieApi.getMovieTheatreAsync(startDate, endDate, Definitions.API_KEY)
    }
}