package gr.pchasapis.moviedb.model.parsers.movie


import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import gr.pchasapis.moviedb.model.parsers.genre.GenresItem
import gr.pchasapis.moviedb.model.parsers.video.Videos
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("videos") val videos: Videos? = null,
    @SerialName("video") val video: Boolean? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("revenue") val revenue: Long? = null,
    @SerialName("genres") val genres: List<GenresItem>? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("id") val id: Int,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("budget") val budget: Long? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("tagline") val tagline: String? = null,
    @SerialName("adult") val adult: Boolean? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("status") val status: String? = null
) : CommonResponse()
