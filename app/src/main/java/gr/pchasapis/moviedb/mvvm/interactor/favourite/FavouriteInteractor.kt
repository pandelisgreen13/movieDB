package gr.pchasapis.moviedb.mvvm.interactor.favourite

import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.database.dao.MovieDbTable
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

interface FavouriteInteractor {

    suspend fun fetchWatchListFromDatabase(): Flow<DataResult<List<HomeDataModel>>>
}

class FavouriteInteractorImpl @Inject constructor(private val movieDbDatabase: MovieDbDatabase) : FavouriteInteractor {

    override suspend fun fetchWatchListFromDatabase(): Flow<DataResult<List<HomeDataModel>>> {
        return try {
            val databaseList = movieDbDatabase.movieDbTableDao().loadAll()
            flow {
                emit(DataResult(toHomeDataModelFromTable(databaseList)))
            }
        } catch (t: Throwable) {
            Timber.d(t)
            flow {
                DataResult(null, throwable = t)
            }
        }
    }

    private fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel> {
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
                    dateAdded = databaseItem.dateAdded)
        }
    }

}
