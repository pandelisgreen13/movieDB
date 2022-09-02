package gr.pchasapis.moviedb.mvvm.interactor.home

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.mvvm.interactor.base.MVVMInteractor
import kotlinx.coroutines.flow.Flow

interface HomeInteractor : MVVMInteractor {

    suspend fun onRetrieveSearchResult(queryText: String, page: Int): Flow<DataResult<List<HomeDataModel>>>
    suspend fun getMoviesInTheatres():DataResult<List<MovieDataModel>>

    fun flowPaging(queryText: String): LiveData<PagingData<HomeDataModel>>
}
