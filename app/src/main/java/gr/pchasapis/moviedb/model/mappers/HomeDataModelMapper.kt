package gr.pchasapis.moviedb.model.mappers

import gr.pchasapis.moviedb.database.dao.MovieDbTable
import gr.pchasapis.moviedb.model.data.HomeDataModel
import javax.inject.Inject

interface HomeDataModelMapper {

    fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel>
}

class HomeDataModelMapperImpl @Inject constructor() : HomeDataModelMapper {

    override fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel> {
        return databaseList.map { databaseItem ->
            HomeDataModel(
                id = databaseItem.id,
                title = databaseItem.title,
                mediaType = databaseItem.mediaType,
                summary = databaseItem.summary,
                thumbnail = databaseItem.thumbnail,
                releaseDate = databaseItem.releaseDate,
                ratings = databaseItem.ratings,
                isFavorite = databaseItem.isFavourite,
                genresName = databaseItem.genresName,
                videoKey = databaseItem.videoKey,
                videoUrl = databaseItem.videoUrl,
                dateAdded = databaseItem.dateAdded
            )
        }
    }
}