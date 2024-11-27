package gr.pchasapis.moviedb.mvvm.interactor.details

import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.SimilarMoviesModel
import gr.pchasapis.moviedb.mvvm.interactor.base.MVVMInteractor
import kotlinx.coroutines.flow.Flow

interface DetailsInteractor : MVVMInteractor {

    suspend fun onRetrieveFlowDetails(homeDataModel: HomeDataModel): Flow<DataResult<HomeDataModel>>
    suspend fun onRetrieveDetails(homeDataModel: HomeDataModel): DataResult<HomeDataModel>
    suspend fun updateFavourite(homeDataModel: HomeDataModel?): DataResult<HomeDataModel>
    suspend fun getSimilarMovies(id: Int, mediaType: String): DataResult<List<SimilarMoviesModel>>
}
