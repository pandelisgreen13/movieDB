package gr.pchasapis.moviedb.database.theaterDao

import androidx.room.Entity
import gr.pchasapis.moviedb.database.base.BaseMovieEntity

@Entity(tableName = "TheaterDbTable")
class TheaterDbTable(
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
    dateAdded: Long = 0,
    val page: Int = 0,
    val totalPage: Int = 0
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