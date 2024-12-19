package gr.pchasapis.moviedb.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDataModel(
    val id: Int? = null,
    val title: String? = "-",
    val summary: String? = "-",
    var thumbnail: String? = "",
    val releaseDate: String? = "-",
    val ratings: String? = "-"
) : Parcelable