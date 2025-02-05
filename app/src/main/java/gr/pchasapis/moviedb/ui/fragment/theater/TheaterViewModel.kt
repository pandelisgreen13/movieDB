package gr.pchasapis.moviedb.ui.fragment.theater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.ui.fragment.favourite.FavouriteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            val response = homeInteractor.getMoviesInTheatres()
            _uiState.update {
                it.copy(
                    loading = false,
                    list = response.data ?: emptyList()
                )
            }
        }
    }
}

data class TheaterUiState(
    val list: List<HomeDataModel> = listOf(),
    val loading: Boolean = true
)