package gr.pchasapis.moviedb.database.dao

import androidx.room.Entity
import gr.pchasapis.moviedb.database.base.BaseMovieEntity

@Entity(tableName = "MovieDbTable")
class MovieDbTable(
    id: Int,
    mediaType: String = "",
    title: String = "-",
    summary: String = "-",
    thumbnail: String = "",
    releaseDate: String = "-",
    ratings: String = "-",
    isFavourite: Boolean = false,
    genresName: String = "-",
    videoUrl: String = "",
    videoKey: String = "",
    genre: String = "",
    dateAdded: Long = 0
) : BaseMovieEntity(
    id = id,
    mediaType = mediaType,
    title = title,
    summary = summary,
    thumbnail = thumbnail,
    releaseDate = releaseDate,
    ratings = ratings,
    isFavourite = isFavourite,
    genresName = genresName,
    videoUrl = videoUrl,
    videoKey = videoKey,
    genre = genre,
    dateAdded = dateAdded
)
