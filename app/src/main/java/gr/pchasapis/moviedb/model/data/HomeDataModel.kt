package gr.pchasapis.moviedb.model.data

import android.os.Parcelable
import gr.pchasapis.moviedb.ui.fragment.favourite.FavouriteFilterEvents
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class HomeDataModel(
    val id: Int,
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

fun List<HomeDataModel>.filterFavourites(favouriteFilterEvents: FavouriteFilterEvents): List<HomeDataModel> {

    return when (favouriteFilterEvents) {
        FavouriteFilterEvents.ByDateAdded -> {
            this.sortedByDescending { it.dateAdded }
        }

        FavouriteFilterEvents.ByName -> {
            this.sortedBy { it.title }
        }

        FavouriteFilterEvents.ByRate -> {
            this.sortedByDescending { it.ratings?.toDouble() ?: 0.0 }
        }
    }
}


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