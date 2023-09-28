package gr.pchasapis.moviedb.mvvm.viewModel.details.compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsComposeViewModel @Inject constructor(private val detailsInteractor: DetailsInteractorImpl) : ViewModel() {

    var homeDataModel: HomeDataModel? = null
    var hasUserChangeFavourite: Boolean? = false

    private var isFavouriteLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val showFavouriteLiveData: LiveData<Boolean>
        get() = isFavouriteLiveData

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    private fun fetchDetails() {
        if (homeDataModel == null ||
                homeDataModel?.id == null ||
                homeDataModel?.mediaType == null) {
            return
        }

        viewModelScope.launch {
            detailsInteractor.onRetrieveFlowDetails(homeDataModel!!).collect { response ->
                response.data?.let {
                    _uiState.value = DetailsUiState.Success(it)
                    isFavouriteLiveData.value = it.isFavorite
                } ?: run {
                    _uiState.value = DetailsUiState.Error
                }
            }
        }
    }

    fun toggleFavourite() {
        if (homeDataModel == null) {
            return
        }
        hasUserChangeFavourite = true
        val homeModel = homeDataModel
        homeModel?.isFavorite = homeModel?.isFavorite == false
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) { detailsInteractor.updateFavourite(homeModel) }
            homeDataModel = response.data
            homeDataModel?.let {
                isFavouriteLiveData.value = it.isFavorite
            }
        }
    }

    fun setUIModel(homeDataModel: HomeDataModel?) {
        this.homeDataModel = homeDataModel
        fetchDetails()
    }
}

sealed class DetailsUiState {

    data object Loading : DetailsUiState()
    data class Success(val homeDataModel: HomeDataModel) : DetailsUiState()
    data object Error : DetailsUiState()
}
