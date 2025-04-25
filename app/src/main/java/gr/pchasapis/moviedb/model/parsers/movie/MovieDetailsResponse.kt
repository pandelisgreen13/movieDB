package gr.pchasapis.moviedb.model.parsers.movie

import com.google.gson.annotations.SerializedName
import gr.pchasapis.moviedb.model.parsers.common.CommonResponse
import gr.pchasapis.moviedb.model.parsers.genre.GenresItem
import gr.pchasapis.moviedb.model.parsers.video.Videos

data class MovieDetailsResponse(@SerializedName("original_language") val originalLanguage: String? = null,
                                @SerializedName("imdb_id") val imdbId: String? = null,
                                @SerializedName("videos") val videos: Videos? = null,
                                @SerializedName("video") val video: Boolean? = null,
                                @SerializedName("title") val title: String? = null,
                                @SerializedName("backdrop_path") val backdropPath: String? = null,
                                @SerializedName("revenue") val revenue: Long? = null,
                                @SerializedName("genres") val genres: List<GenresItem>? = null,
                                @SerializedName("popularity") val popularity: Double? = null,
                                @SerializedName("id") val id: Int,
                                @SerializedName("vote_count") val voteCount: Int? = null,
                                @SerializedName("budget") val budget: Long? = null,
                                @SerializedName("overview") val overview: String? = null,
                                @SerializedName("original_title") val originalTitle: String? = null,
                                @SerializedName("runtime") val runtime: Int? = null,
                                @SerializedName("poster_path") val posterPath: String? = null,
                                @SerializedName("release_date") val releaseDate: String? = null,
                                @SerializedName("vote_average") val voteAverage: Double? = null,
                                @SerializedName("tagline") val tagline: String? = null,
                                @SerializedName("adult") val adult: Boolean? = null,
                                @SerializedName("homepage") val homepage: String? = null,
                                @SerializedName("status") val status: String? = null) : CommonResponse()