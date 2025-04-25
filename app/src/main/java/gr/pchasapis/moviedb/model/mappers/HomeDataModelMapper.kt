package gr.pchasapis.moviedb.model.mappers

import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.database.dao.MovieDbTable
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.theatre.MovieNetworkResponse
import javax.inject.Inject

interface HomeDataModelMapper {

    fun toHomeDataModelFromTheater(response: MovieNetworkResponse): List<HomeDataModel>
    fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel>
    suspend fun toHomeDataModelFromResponse(searchResponse: SearchResponse): List<HomeDataModel>
}

class HomeDataModelMapperImpl @Inject constructor(private val movieDbDatabase: MovieDbDatabase) :
    HomeDataModelMapper {

    override fun toHomeDataModelFromTheater(response: MovieNetworkResponse): List<HomeDataModel> {
        return (response.searchResultsList?.filter { it.id != null }?.map { movieItem ->
            HomeDataModel(
                id = movieItem.id!!,
                title = movieItem.title ?: "-",
                mediaType = "movie",
                summary = movieItem.overview ?: "-",
                thumbnail = "${Definitions.IMAGE_URL_W300}${movieItem.posterPath}",
                ratings = (movieItem.voteAverage ?: 0).toString(),
                releaseDate = movieItem.releaseDate ?: "-",
                isFavorite = movieDbDatabase.movieDbTableDao().isFavourite(movieItem.id ?: 0)
            )
        } ?: arrayListOf())
    }


    override fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel> {
        return databaseList.map { databaseItem ->
            mapFromDB(databaseItem)
        }
    }

    private fun mapFromDB(databaseItem: MovieDbTable) =
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

    override suspend fun toHomeDataModelFromResponse(searchResponse: SearchResponse): List<HomeDataModel> {
        return (searchResponse.searchResultsList?.map { searchItem ->
            HomeDataModel(
                id = searchItem.id,
                title = searchItem.title ?: searchItem.name ?: searchItem.originalName ?: "-",
                mediaType = searchItem.mediaType ?: "-",
                summary = searchItem.overview ?: "-",
                thumbnail = "${Definitions.IMAGE_URL_W300}${searchItem.posterPath}",
                releaseDate = searchItem.releaseDate ?: searchItem.firstAirDate ?: "-",
                ratings = (searchItem.voteAverage ?: 0).toString(),
                page = searchResponse.page ?: 0,
                totalPage = searchResponse.totalPages ?: 0,
                isFavorite = movieDbDatabase.movieDbTableDao().isFavourite(searchItem.id ?: 0)
            )
        } ?: arrayListOf())
    }
}
