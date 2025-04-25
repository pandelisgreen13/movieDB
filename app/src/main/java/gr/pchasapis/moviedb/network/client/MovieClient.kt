package gr.pchasapis.moviedb.network.client

import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.model.parsers.movie.MovieDetailsResponse
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.theatre.MovieNetworkResponse
import gr.pchasapis.moviedb.model.parsers.tv.TvShowResponse
import gr.pchasapis.moviedb.network.api.MovieApi
import javax.inject.Inject

class MovieClient @Inject constructor(private var movieApi: MovieApi) {

    suspend fun getSearchAsync(queryText: String, page: Int): SearchResponse {
        return movieApi.getSearchAsync(Definitions.API_KEY, queryText, page)
    }

    suspend fun getMovieDetailsAsync(movieId: Int): MovieDetailsResponse {
        return movieApi.getMovieDetailsAsync(movieId, Definitions.API_KEY, Definitions.VIDEOS)
    }

    suspend fun getTvShowDetailsAsync(tvId: Int): TvShowResponse {
        return movieApi.getTvShowDetailsAsync(tvId, Definitions.API_KEY, Definitions.VIDEOS)
    }

    suspend fun getMovieTheatre(page: Int = 1): MovieNetworkResponse {
        return movieApi.getMovieTheatreAsync(page = page, apiKey = Definitions.API_KEY)
    }

    suspend fun getSimilarMovies(id: Int, mediaType: String): SearchResponse {

        return movieApi.getSimilarMovies(id, mediaType, Definitions.API_KEY)
    }
}