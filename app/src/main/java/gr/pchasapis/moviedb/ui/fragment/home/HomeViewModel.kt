package gr.pchasapis.moviedb.ui.fragment.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.common.SingleLiveEvent
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractorImpl
) : BaseViewModel() {

    private var theatreMutableLiveData: SingleLiveEvent<MutableList<MovieDataModel>> =
        SingleLiveEvent()

    private var queryTextState = ""

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState


    fun setQueryText(queryText: String) = viewModelScope.launch {
        if (queryText.trim() == queryTextState) {
            return@launch
        }
        queryTextState = queryText
        _uiState.update { it.copy(isLoading = true) }

        val resul = homeInteractor.flowPaging(queryText).cachedIn(viewModelScope)
        _uiState.update { it.copy(isLoading = false, data = resul) }
    }


    fun fetchMovieInTheatre() {
        loadingLiveData.value = true
        uiScope.launch {
            val response = homeInteractor.getMoviesInTheatres()
            loadingLiveData.value = false
            theatreMutableLiveData.value = response.data?.toMutableList()
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val data: Flow<PagingData<HomeDataModel>>? = null
)