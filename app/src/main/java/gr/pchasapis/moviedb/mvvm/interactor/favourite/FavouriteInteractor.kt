package gr.pchasapis.moviedb.mvvm.interactor.favourite

import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

interface FavouriteInteractor {

    fun fetchWatchListFromDatabase(): Flow<List<HomeDataModel>>
}

class FavouriteInteractorImpl @Inject constructor(
    private val movieDbDatabase: MovieDbDatabase,
    private val mapper: HomeDataModelMapperImpl
) : FavouriteInteractor {

    override fun fetchWatchListFromDatabase(): Flow<List<HomeDataModel>> {
        return movieDbDatabase.movieDbTableDao().loadAll().map {
                mapper.toHomeDataModelFromTable(it)
            }.distinctUntilChanged()

    }
}
