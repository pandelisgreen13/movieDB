package gr.pchasapis.moviedb.model.parsers.tv


import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import gr.pchasapis.moviedb.model.parsers.genre.GenresItem
import gr.pchasapis.moviedb.model.parsers.video.Videos
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowResponse(
    @SerialName("videos") val videos: Videos? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("genres") val genres: List<GenresItem>? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("status") val status: String? = null
) : CommonResponse()
