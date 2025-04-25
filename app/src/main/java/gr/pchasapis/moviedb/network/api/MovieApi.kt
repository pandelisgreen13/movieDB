package gr.pchasapis.moviedb.network.api

import gr.pchasapis.moviedb.model.parsers.movie.MovieDetailsResponse
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.theatre.MovieNetworkResponse
import gr.pchasapis.moviedb.model.parsers.tv.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {

    @GET("search/multi")
    suspend fun getSearchAsync(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse


    @GET("movie/{movieId}")
    suspend fun getMovieDetailsAsync(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") appendToResponse: String
    ): MovieDetailsResponse


    @GET("tv/{tvId}")
    suspend fun getTvShowDetailsAsync(
        @Path("tvId") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") appendToResponse: String
    ): TvShowResponse

    @GET("discover/movie")
    suspend fun getMovieTheatreAsync(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
    ): MovieNetworkResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieNetworkResponse

    @GET("{type}/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: Int,
        @Path("type") type: String,
        @Query("api_key") apiKey: String
    ): SearchResponse
}