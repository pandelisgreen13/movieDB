package gr.pchasapis.moviedb.mvvm.interactor.home

import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.mvvm.interactor.base.MVVMInteractor

interface HomeInteractor : MVVMInteractor {

    suspend fun onRetrieveSearchResult(queryText: String, page: Int): DataResult<List<HomeDataModel>>
    suspend fun getWatchList(): DataResult<List<HomeDataModel>>
    suspend fun getMoviesInTheatres():DataResult<List<MovieDataModel>>
}