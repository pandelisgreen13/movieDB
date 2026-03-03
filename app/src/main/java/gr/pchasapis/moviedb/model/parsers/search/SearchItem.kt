package gr.pchasapis.moviedb.model.parsers.search


import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchItem(
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("origin_country") val originCountry: List<String>? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("id") val id: Int,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("video") val video: Boolean? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("adult") val adult: Boolean? = null
) : CommonResponse()
