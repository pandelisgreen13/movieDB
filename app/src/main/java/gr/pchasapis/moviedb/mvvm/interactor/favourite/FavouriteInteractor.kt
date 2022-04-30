package gr.pchasapis.moviedb.mvvm.interactor.favourite

import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

interface FavouriteInteractor {

    suspend fun fetchWatchListFromDatabase(): Flow<DataResult<List<HomeDataModel>>>
}

class FavouriteInteractorImpl @Inject constructor(
    private val movieDbDatabase: MovieDbDatabase,
    private val mapper: HomeDataModelMapperImpl
) : FavouriteInteractor {

    override suspend fun fetchWatchListFromDatabase(): Flow<DataResult<List<HomeDataModel>>> {
        return try {
            val databaseList = movieDbDatabase.movieDbTableDao().loadAll()
            flow {
                emit(DataResult(mapper.toHomeDataModelFromTable(databaseList)))
            }
        } catch (t: Throwable) {
            Timber.d(t)
            flow {
                DataResult(null, throwable = t)
            }
        }
    }
}
