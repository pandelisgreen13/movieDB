package gr.pchasapis.moviedb.ui.fragment.theater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TheaterViewModel @Inject constructor(
    private val homeInteractor: HomeInteractorImpl
) : ViewModel() {


    fun fetchMovieInTheatre() {
        viewModelScope.launch {
            val response = homeInteractor.getMoviesInTheatres()
        }
    }
}