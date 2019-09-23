package gr.pchasapis.moviedb.mvvm.viewModel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractor
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DetailsViewModel(private val detailsInteractor: DetailsInteractor, var homeDataModel: HomeDataModel?) : BaseViewModel() {

    private lateinit var homeDataModelLiveData: MutableLiveData<HomeDataModel>
    private var isFavouriteLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var hasUserChangeFavourite: Boolean? = false

    fun getDetailsList(): LiveData<HomeDataModel> {
        if (!::homeDataModelLiveData.isInitialized) {
            homeDataModelLiveData = MutableLiveData()
            fetchDetails()
        }
        return homeDataModelLiveData
    }

    fun getFavourite(): LiveData<Boolean> {
        return isFavouriteLiveData
    }

    private fun fetchDetails() {
        if (homeDataModel == null ||
                homeDataModel?.id == null ||
                homeDataModel?.mediaType == null) {
            return
        }

        uiScope.launch {
            loadingLiveData.value = true
            val response = withContext(bgDispatcher) { detailsInteractor.onRetrieveDetails(homeDataModel!!) }
            response.data?.let {
                homeDataModelLiveData.value = it
            } ?: response.throwable?.let {
                Timber.e(it.toString())
                onErrorThrowable(it, true)
            } ?: run {
                genericErrorLiveData.value = true
            }
            loadingLiveData.value = false
        }
    }

    fun toggleFavourite() {
        if (homeDataModel == null) {
            return
        }
        hasUserChangeFavourite = true
        val homeModel = homeDataModel
        homeModel?.isFavorite = homeModel?.isFavorite == false
        uiScope.launch {
            loadingLiveData.value = true
            val response = withContext(bgDispatcher) { detailsInteractor.updateFavourite(homeModel) }
            homeDataModel = response.data
            homeDataModel?.let {
                isFavouriteLiveData.value = it.isFavorite
            } ?: response.throwable?.let {
                Timber.e(it.toString())
                genericErrorLiveData.value = true
            } ?: run {
                genericErrorLiveData.value = true
            }
            loadingLiveData.value = false
        }
    }
}