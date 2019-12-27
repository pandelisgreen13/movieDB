package gr.pchasapis.moviedb.mvvm.interactor.home

import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.database.dao.MovieDbTable
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.mvvm.interactor.base.BaseInteractor
import gr.pchasapis.moviedb.network.client.MovieClient
import timber.log.Timber


class HomeInteractorImpl(private var movieClient: MovieClient, private val movieDbDatabase: MovieDbDatabase) : BaseInteractor(), HomeInteractor {

    override suspend fun getWatchList(): DataResult<List<HomeDataModel>> {
        return try {
            val databaseList = movieDbDatabase.movieDbTableDao().loadAll()
            DataResult(toHomeDataModelFromTable(databaseList))
        } catch (t: Throwable) {
            Timber.d(t)
            DataResult(throwable = t)
        }
    }

    override suspend fun onRetrieveSearchResult(queryText: String, page: Int): DataResult<List<HomeDataModel>> {
        return try {
            val response = movieClient.getSearchAsync(queryText, page)
            DataResult(toHomeDataModel(response))
        } catch (t: Throwable) {
            Timber.d(t)
            DataResult(throwable = t)
        }
    }

    private fun toHomeDataModel(searchResponse: SearchResponse): List<HomeDataModel> {
        return (searchResponse.searchResultsList?.map { searchItem ->
            return@map HomeDataModel(
                    id = searchItem.id,
                    title = searchItem.title ?: searchItem.name ?: searchItem.originalName ?: "-",
                    mediaType = searchItem.mediaType ?: "-",
                    summary = searchItem.overview ?: "-",
                    thumbnail = "${Definitions.IMAGE_URL_W300}${searchItem.posterPath}",
                    releaseDate = searchItem.releaseDate ?: searchItem.firstAirDate ?: "-",
                    ratings = (searchItem.voteAverage ?: 0).toString(),
                    page = searchResponse.page ?: 0,
                    totalPage = searchResponse.totalPages ?: 0,
                    isFavorite = movieDbDatabase.movieDbTableDao().isFavourite(searchItem.id ?: 0))
        } ?: arrayListOf())
    }

    private fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel>? {
        return databaseList.map { databaseItem ->
            return@map HomeDataModel(
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
                    dateAdded = databaseItem.dateAdded)
        }
    }


}