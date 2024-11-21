package gr.pchasapis.moviedb.mvvm.viewModel.details.compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.SimilarMoviesModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsComposeViewModel @Inject constructor(private val detailsInteractor: DetailsInteractorImpl) :
    ViewModel() {

    private var homeDataModel: HomeDataModel? = null
    var hasUserChangeFavourite: Boolean? = false

    private var isFavouriteLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val showFavouriteLiveData: LiveData<Boolean>
        get() = isFavouriteLiveData

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    private fun fetchDetails() {
        if (homeDataModel == null ||
            homeDataModel?.mediaType == null
        ) {
            return
        }

        homeDataModel?.id?.let { id ->
            viewModelScope.launch {
                detailsInteractor.onRetrieveFlowDetails(homeDataModel!!).collectLatest { response ->
                    response.data?.let { data ->
                        _uiState.update {
                            DetailsUiState.Success(data)
                        }
                        isFavouriteLiveData.value = data.isFavorite
                    } ?: run {
                        _uiState.value = DetailsUiState.Error
                    }
                }

                val response = detailsInteractor.getSimilarMovies(id)
                response.data?.let {
                    _uiState.update { state ->
                        (state as? DetailsUiState.Success)?.copy(
                            similarMovies = response.data
                        ) ?: state
                    }
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
            val response =
                withContext(Dispatchers.IO) { detailsInteractor.updateFavourite(homeModel) }
            homeDataModel = response.data
            homeDataModel?.let {
                isFavouriteLiveData.value = it.isFavorite
            }
        }
    }

    fun setUIModel(homeDataModel: HomeDataModel?) {
        if (this.homeDataModel != null) {
            return
        }
        this.homeDataModel = homeDataModel
        fetchDetails()
    }
}

sealed class DetailsUiState {

    data object Loading : DetailsUiState()
    data class Success(
        val homeDataModel: HomeDataModel,
        val similarMovies: List<SimilarMoviesModel> = emptyList()
    ) : DetailsUiState()

    data object Error : DetailsUiState()
}
