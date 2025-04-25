package gr.pchasapis.moviedb.mvvm.interactor.home.theater

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.database.keysDao.RemoteKey
import gr.pchasapis.moviedb.database.theaterDao.TheaterDbTable
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import gr.pchasapis.moviedb.model.parsers.theatre.MovieNetworkResponse
import gr.pchasapis.moviedb.network.client.MovieClient
import timber.log.Timber


private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class TheaterRemoteMediator(
    private val movieClient: MovieClient,
    private val mapper: HomeDataModelMapperImpl,
    private val database: MovieDbDatabase
) : RemoteMediator<Int, TheaterDbTable>() {

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TheaterDbTable>
    ): MediatorResult {
        return try {
            Timber.d("pagination -> $loadType")
            val loadKey = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> {

                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                }

                LoadType.APPEND -> {

                    val daoKey = database.remoteKeyDao().loadAll()

                    Timber.d("pagination -> keys database $${daoKey}")
                    Timber.d("pagination -> firstItem $${state.firstItemOrNull()?.page}")

                    daoKey.last().nextKey + 1
                }
            }

            Timber.d("pagination loadKey: -> $loadKey")

            if (loadKey > 3) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = movieClient.getMovieTheatre(page = loadKey)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.theaterDbTableDao().deleteAll()
                    database.remoteKeyDao().deleteAll()
                }

                Timber.d("pagination save page: -> ${response.page}")
                Timber.d("pagination =====================  page: ${response.page}   =============================================")
                database.theaterDbTableDao().insertAll(toDatabaseModel(response))
                response.page.let { page ->
                    database.remoteKeyDao().insertOrReplace(
                        RemoteKey(
                            nextKey = page
                        )
                    )
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.searchResultsList?.isEmpty() == true
                        || response.page == response.totalPages
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    private fun toDatabaseModel(response: MovieNetworkResponse): List<TheaterDbTable> {
        return response.searchResultsList?.map { model ->
            TheaterDbTable(
                id = model.id,
                title = model.title ?: "-",
                mediaType = "movie",
                summary = model.overview ?: "-",
                thumbnail = model.posterPath ?: "",
                releaseDate = model.releaseDate ?: "-",
                ratings = (model.voteAverage ?: 0).toString(),
                page = response.page,
                totalPage = response.totalPages ?: 0,
                dateAdded = System.currentTimeMillis()
            )
        } ?: listOf()
    }
}