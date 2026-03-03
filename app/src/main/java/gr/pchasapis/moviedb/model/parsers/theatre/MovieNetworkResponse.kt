package gr.pchasapis.moviedb.model.parsers.theatre


import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import gr.pchasapis.moviedb.model.parsers.movie.MovieDetailsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieNetworkResponse(
    @SerialName("page") val page: Int,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("results") val searchResultsList: List<MovieDetailsResponse>? = null,
    @SerialName("total_results") val totalResults: Int? = null
) : CommonResponse()
