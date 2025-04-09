package gr.pchasapis.moviedb.model.parsers.theatre

import com.google.gson.annotations.SerializedName
import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import gr.pchasapis.moviedb.model.parsers.movie.MovieDetailsResponse

data class MovieNetworkResponse(@SerializedName("page") val page: Int,
                                @SerializedName("total_pages") val totalPages: Int? = null,
                                @SerializedName("results") val searchResultsList: List<MovieDetailsResponse>? = null,
                                @SerializedName("total_results") val totalResults: Int? = null) : CommonResponse()