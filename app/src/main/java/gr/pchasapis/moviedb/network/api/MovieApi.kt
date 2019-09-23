package gr.pchasapis.moviedb.network.api

import gr.pchasapis.moviedb.model.parsers.movie.MovieResponse
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.tv.TvShowResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {

    @GET("search/multi")
    fun getSearchAsync(@Query("api_key") apiKey: String,
                       @Query("query") query: String,
                       @Query("page") page: Int): Deferred<SearchResponse>


    @GET("movie/{movieId}")
    fun getMovieDetailsAsync(@Path("movieId") movieId: Int,
                             @Query("api_key") apiKey: String,
                             @Query("append_to_response") appendToResponse: String): Deferred<MovieResponse>


    @GET("tv/{tvId}")
    fun getTvShowDetailsAsync(@Path("tvId") movieId: Int,
                              @Query("api_key") apiKey: String,
                              @Query("append_to_response") appendToResponse: String): Deferred<TvShowResponse>

}