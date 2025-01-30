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

    private var isFavouriteLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val showFavouriteLiveData: LiveData<Boolean>
        get() = isFavouriteLiveData

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    private fun fetchDetails(model: HomeDataModel?) {
        if (model?.mediaType == null) {
            _uiState.value = DetailsUiState.Error
            return
        }

        model.id?.let { id ->
            viewModelScope.launch {
                isFavouriteLiveData.value = model.isFavorite

                detailsInteractor.onRetrieveFlowDetails(model).collectLatest { response ->
                    response.data?.let { data ->
                        _uiState.update {
                            DetailsUiState.Success(data)
                        }
                    } ?: run {
                        _uiState.value = DetailsUiState.Success(this@DetailsComposeViewModel.homeDataModel!!)
                    }
                }
                val response = detailsInteractor.getSimilarMovies(
                    id = id,
                    mediaType = this@DetailsComposeViewModel.homeDataModel?.mediaType.orEmpty()
                )
                _uiState.update { state ->
                    (state as? DetailsUiState.Success)?.copy(
                        similarMovies = response.data.orEmpty()
                    ) ?: state
                }
            }
        }


    }

    fun toggleFavourite() {
        val homeModel = (uiState.value as? DetailsUiState.Success)?.homeDataModel ?: return

        homeModel.isFavorite = homeModel.isFavorite == false

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
        fetchDetails(homeDataModel)
    }
}

sealed class DetailsUiState {

    data object Loading : DetailsUiState()
    data class Success(
        val homeDataModel: HomeDataModel,
        val similarMovies: List<SimilarMoviesModel>? = null
    ) : DetailsUiState()

    data object Error : DetailsUiState()
}
