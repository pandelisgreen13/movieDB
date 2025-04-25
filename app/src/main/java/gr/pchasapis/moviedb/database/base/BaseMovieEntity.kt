package gr.pchasapis.moviedb.database.base

import androidx.room.PrimaryKey

open class BaseMovieEntity(
    @PrimaryKey
    var id: Int,
    var mediaType: String = "",
    var title: String = "-",
    var summary: String = "-",
    var thumbnail: String = "",
    var releaseDate: String = "-",
    var ratings: String = "-",
    var isFavourite: Boolean = false,
    var genresName: String = "-",
    var videoUrl: String = "",
    var videoKey: String = "",
    var genre: String = "",
    var dateAdded: Long = 0
)