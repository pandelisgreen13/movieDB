package gr.pchasapis.moviedb.model.parsers.theatre

import com.google.gson.annotations.SerializedName
import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import gr.pchasapis.moviedb.model.parsers.movie.MovieResponse

data class TheatreResponse(@SerializedName("page") val page: Int? = null,
                           @SerializedName("total_pages") val totalPages: Int? = null,
                           @SerializedName("results") val searchResultsList: List<MovieResponse>? = null,
                           @SerializedName("total_results") val totalResults: Int? = null) : CommonResponse()