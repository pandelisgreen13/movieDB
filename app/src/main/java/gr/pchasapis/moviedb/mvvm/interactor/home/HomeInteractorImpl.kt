package gr.pchasapis.moviedb.mvvm.interactor.home

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.database.theaterDao.TheaterDbTable
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import gr.pchasapis.moviedb.model.parsers.search.SearchResponse
import gr.pchasapis.moviedb.model.parsers.theatre.MovieNetworkResponse
import gr.pchasapis.moviedb.mvvm.interactor.home.paging.SearchPagingDataSource
import gr.pchasapis.moviedb.mvvm.interactor.home.theater.TheaterRemoteMediator
import gr.pchasapis.moviedb.network.client.MovieClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber


class HomeInteractorImpl(
    private var movieClient: MovieClient,
    private val movieDbDatabase: MovieDbDatabase,
    private val mapper: HomeDataModelMapperImpl
) : HomeInteractor {

    fun deleteDatabase() {
        movieDbDatabase.theaterDbTableDao().deleteAll()
    }

    override suspend fun onRetrieveSearchResult(
        queryText: String,
        page: Int
    ): Flow<DataResult<List<HomeDataModel>>> {
        return try {
            val response = movieClient.getSearchAsync(queryText, page)
            flow {
                emit(DataResult(toHomeDataModel(response)))
            }
        } catch (t: Throwable) {
            Timber.d(t)
            flow { DataResult(null, throwable = t) }
        }
    }

    override suspend fun getMoviesInTheatres(): DataResult<List<HomeDataModel>> {
        return try {
            val response = movieClient.getMovieTheatre()
            DataResult(toMovieDataModel(response))
        } catch (t: Throwable) {
            Timber.d(t)
            DataResult(throwable = t)
        }
    }

    override suspend fun flowPaging(queryText: String): Flow<PagingData<HomeDataModel>> {


        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            PagingConfig(
                pageSize = 20
            )
        ) {
            SearchPagingDataSource(queryText, movieClient, mapper)
        }.flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun flowTheater(): Pager<Int, TheaterDbTable> {
        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            config = PagingConfig(pageSize = 10),
            remoteMediator = TheaterRemoteMediator(movieClient, mapper, movieDbDatabase)
        ) {
            movieDbDatabase.theaterDbTableDao().pagingSource()
        }
    }

    private fun toHomeDataModel(searchResponse: SearchResponse): List<HomeDataModel> {
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

    private fun toMovieDataModel(theatreResponse: MovieNetworkResponse): List<HomeDataModel> {
        return (theatreResponse.searchResultsList?.map { movieItem ->
            HomeDataModel(
                id = movieItem.id,
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

    companion object {

        const val DATE_FROM = "2019-12-22"
        const val DATE_TO = "2019-12-31"
    }

}
