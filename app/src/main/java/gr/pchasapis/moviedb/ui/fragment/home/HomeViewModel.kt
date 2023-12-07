package gr.pchasapis.moviedb.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.common.SingleLiveEvent
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractorImpl
) : BaseViewModel() {

    private var theatreMutableLiveData: SingleLiveEvent<MutableList<MovieDataModel>> =
        SingleLiveEvent()

    private var queryText = ""

    fun getMovieInTheatre(): SingleLiveEvent<MutableList<MovieDataModel>> {
        return theatreMutableLiveData
    }

    private val currentQuery = MutableLiveData("")

    fun getMovies() = movies.asFlow()

   val movies = currentQuery.switchMap {
        if (it.isEmpty()) {
            MutableLiveData()
        } else {
            homeInteractor.flowPaging(queryText).cachedIn(viewModelScope)
        }
    }


    fun searchMovies() {
        currentQuery.value = queryText
    }

    fun setQueryText(queryText: String) {
        this.queryText = queryText
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
