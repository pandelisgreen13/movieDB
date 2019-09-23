package gr.pchasapis.moviedb.mvvm.interactor.details

import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.base.MVVMInteractor

interface DetailsInteractor : MVVMInteractor {

    suspend fun onRetrieveDetails(homeDataModel: HomeDataModel): DataResult<HomeDataModel>
    suspend fun updateFavourite(homeDataModel: HomeDataModel?): DataResult<HomeDataModel>
}