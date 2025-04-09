package gr.pchasapis.moviedb.ui.fragment.theater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TheaterViewModel @Inject constructor(
    private val homeInteractor: HomeInteractorImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(TheaterUiState())

    val uiState: StateFlow<TheaterUiState> = _uiState

    init {
        fetchMovieInTheatre()
    }

    private fun fetchMovieInTheatre() {
        viewModelScope.launch {
            //  val response = homeInteractor.getMoviesInTheatres()
            val response = homeInteractor.flowTheater().flow
                .map { pagingData ->
                    pagingData.map {
                        HomeDataModel(
                            id = it.id,
                            title = it.title,
                            thumbnail = "${Definitions.IMAGE_URL_W500}${it.thumbnail}",
                            mediaType = it.mediaType,
                            summary = it.summary
                        )
                    }

                }.cachedIn(viewModelScope)

            _uiState.update {
                it.copy(
                    loading = false,
                    list = response
                )
            }
        }
    }

    fun deleteDatabase() {
        homeInteractor.deleteDatabase()
    }
}

data class TheaterUiState(
    val list: Flow<PagingData<HomeDataModel>>? = null,
    val loading: Boolean = true
)