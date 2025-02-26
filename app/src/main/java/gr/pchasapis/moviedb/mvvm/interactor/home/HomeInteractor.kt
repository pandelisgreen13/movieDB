package gr.pchasapis.moviedb.mvvm.interactor.home

import androidx.paging.Pager
import androidx.paging.PagingData
import gr.pchasapis.moviedb.database.theaterDao.TheaterDbTable
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import kotlinx.coroutines.flow.Flow

interface HomeInteractor {

    suspend fun onRetrieveSearchResult(
        queryText: String,
        page: Int
    ): Flow<DataResult<List<HomeDataModel>>>

    suspend fun getMoviesInTheatres(): DataResult<List<HomeDataModel>>

    suspend fun flowPaging(queryText: String): Flow<PagingData<HomeDataModel>>
    fun flowTheater(): Pager<Int, TheaterDbTable>
}
