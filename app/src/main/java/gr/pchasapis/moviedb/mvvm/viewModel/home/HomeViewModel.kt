package gr.pchasapis.moviedb.mvvm.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.common.SingleLiveEvent
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.mvvm.interactor.home.paging.SearchPagingDataSource
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel
import gr.pchasapis.moviedb.network.client.MovieClient
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractorImpl,
    private var movieClient: MovieClient,
    private val mapper: HomeDataModelMapperImpl
) : BaseViewModel() {

    private lateinit var searchMutableLiveData: MutableLiveData<MutableList<HomeDataModel>>
    private var theatreMutableLiveData: SingleLiveEvent<MutableList<MovieDataModel>> =
        SingleLiveEvent()
    private var finishPaginationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var paginationLoaderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var searchList = mutableListOf<HomeDataModel>()
    private var databaseList = mutableListOf<HomeDataModel>()
    private var page = 0
    private var isFetching = false
    private var queryText = ""

    fun getSearchList(): LiveData<MutableList<HomeDataModel>> {
        if (!::searchMutableLiveData.isInitialized) {
            searchMutableLiveData = MutableLiveData()
            // fetchSearchResult()
        }
        return searchMutableLiveData
    }

    fun getMovieInTheatre(): SingleLiveEvent<MutableList<MovieDataModel>> {
        return theatreMutableLiveData
    }

    fun getPaginationStatus(): LiveData<Boolean> {
        return finishPaginationLiveData
    }

    fun getPaginationLoader(): LiveData<Boolean> {
        return paginationLoaderLiveData
    }

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 20)
    ) {
        SearchPagingDataSource(queryText, movieClient, mapper)
    }.flow.cachedIn(viewModelScope)

    private val currentQuery = MutableLiveData("")

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

    fun fetchSearchResult() {
        if (queryText.isEmpty()) {
            return
        }

        uiScope.launch {
            if (page == 0) {
                loadingLiveData.value = true
            }
            page++

            homeInteractor.onRetrieveSearchResult(queryText, page).collect { response ->

                response.data?.let {
                    emptyLiveData.value = false
                    finishPaginationLiveData.value = isPaginationFinished(
                        it.firstOrNull()?.page
                            ?: 0, it.firstOrNull()?.totalPage ?: 0
                    )
                    searchList.addAll(it)
                    searchMutableLiveData.value = searchList
                } ?: run {
                    Timber.e(response.throwable.toString())
                    onErrorThrowable(response.throwable)
                    finishPaginationLiveData.value = true
                }
                paginationLoaderLiveData.value = false
                loadingLiveData.value = false
                setFetching(false)
            }
        }
    }

    @Synchronized
    private fun isFetching(): Boolean {
        return this.isFetching
    }

    @Synchronized
    private fun setFetching(isFetching: Boolean) {
        this.isFetching = isFetching
    }

    private fun resetPagination() {
        if (queryText.isEmpty()) {
            return
        }
        page = 0
        searchList.clear()
    }

    fun setQueryText(queryText: String) {
        this.queryText = queryText
    }

    private fun isPaginationFinished(page: Int, totalPages: Int): Boolean {
        return page >= totalPages
    }

    fun updateModel(updatedHomeDataModel: HomeDataModel?) {
        if (updatedHomeDataModel?.id == null) {
            return
        }
        searchList.map {
            return@map when (it.id) {
                updatedHomeDataModel.id -> it.isFavorite = updatedHomeDataModel.isFavorite
                else -> it
            }
        }
    }

    fun searchForResults() {
        fetchSearchResult()
        resetPagination()
    }

    private fun filterListByQuery() {
        if (queryText.isEmpty()) {
            searchMutableLiveData.value = databaseList
            return
        }
        val queryList = databaseList.filter {
            it.title?.lowercase(Locale.getDefault())
                ?.contains(queryText.toLowerCase(Locale.getDefault()), true)
                ?: false
        }
        searchMutableLiveData.value = queryList.toMutableList()
    }

    fun fetchMovieInTheatre() {
        loadingLiveData.value = true
        uiScope.launch {
            val response = homeInteractor.getMoviesInTheatres()
            loadingLiveData.value = false
            response.data?.let {
                theatreMutableLiveData.value = it.toMutableList()
            } ?: response.throwable?.let {
                Timber.e(it.toString())
                searchMutableLiveData.value = searchList
            } ?: run {
                searchMutableLiveData.value = searchList
            }
        }
    }
}
