package gr.pchasapis.moviedb.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class HomeDataModel(
    val id: Int? = null,
    val mediaType: String? = "",
    val title: String? = "-",
    val summary: String? = "-",
    var thumbnail: String? = "",
    val releaseDate: String? = "-",
    val ratings: String? = "-",
    var isFavorite: Boolean = false,
    var genresName: String? = "-",
    var videoUrl: String? = "",
    var videoKey: String? = "",
    var page: Int = 0,
    var totalPage: Int = 0,
    var dateAdded: Long = 0
) : Parcelable


data class SimilarMoviesModel(
    val id: Int = 0,
    val image: String = ""
) {

    fun toHomeDataModel(mediaType: String?): HomeDataModel {
        return HomeDataModel(
            id = id,
            thumbnail = image,
            mediaType = mediaType ?: "movie"
        )
    }
}