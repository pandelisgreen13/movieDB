package gr.pchasapis.moviedb.mvvm.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractor
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(private val homeInteractor: HomeInteractor) : BaseViewModel() {

    private lateinit var searchMutableLiveData: MutableLiveData<MutableList<HomeDataModel>>
    private lateinit var watchListLiveData: MutableLiveData<Boolean>
    private var theatreMutableLiveData: MutableLiveData<MutableList<MovieDataModel>> = MutableLiveData()
    private var finishPaginationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var toolbarTitleLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var paginationLoaderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var searchList = mutableListOf<HomeDataModel>()
    private var databaseList = mutableListOf<HomeDataModel>()
    private var page = 0
    private var isFetching = false
    private var queryText = ""
    var isWatchListMode = false

    fun getWatchListLiveData(): LiveData<Boolean> {
        if (!::watchListLiveData.isInitialized) {
            watchListLiveData = MutableLiveData()
            readWatchListFromDatabase()
        }
        return watchListLiveData
    }

    fun getSearchList(): LiveData<MutableList<HomeDataModel>> {
        if (!::searchMutableLiveData.isInitialized) {
            searchMutableLiveData = MutableLiveData()
            fetchSearchResult()
        }
        return searchMutableLiveData
    }

    fun getMovieInTheatre(): LiveData<MutableList<MovieDataModel>> {
        return theatreMutableLiveData
    }

    fun getPaginationStatus(): LiveData<Boolean> {
        return finishPaginationLiveData
    }

    fun getToolbarTitle(): LiveData<Boolean> {
        return toolbarTitleLiveData
    }

    fun getPaginationLoader(): LiveData<Boolean> {
        return paginationLoaderLiveData
    }

    fun readWatchListFromDatabase() {
        uiScope.launch {
            val response = withContext(bgDispatcher) { homeInteractor.getWatchList() }
            response.data?.let {
                watchListLiveData.value = it.isNotEmpty()
                databaseList = it.toMutableList()
                if (isWatchListMode && it.isNotEmpty()) {
                    searchMutableLiveData.value = databaseList
                } else {
                    isWatchListMode = false
                    searchMutableLiveData.value = searchList
                }
            } ?: response.throwable?.let {
                Timber.e(it.toString())
                isWatchListMode = false
                searchMutableLiveData.value = searchList
            } ?: run {
                isWatchListMode = false
                searchMutableLiveData.value = searchList
            }
        }
    }

    fun fetchSearchResult() {
        if (isFetching() || queryText.isEmpty() || isWatchListMode) {
            return
        }
        setFetching(true)

        uiScope.launch {
            if (page == 0) {
                loadingLiveData.value = true
            }
            page++
            val response = homeInteractor.onRetrieveSearchResult(queryText, page)
            response.data?.let {
                emptyLiveData.value = false
                finishPaginationLiveData.value = isPaginationFinished(it.firstOrNull()?.page
                        ?: 0, it.firstOrNull()?.totalPage ?: 0)
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
            return@map when {
                it.id == updatedHomeDataModel.id -> it.isFavorite = updatedHomeDataModel.isFavorite
                else -> it
            }
        }
    }

    fun showWatchList() {
        if (databaseList.isEmpty()) {
            return
        }
        isWatchListMode = !isWatchListMode
        searchMutableLiveData.value = when {
            isWatchListMode -> databaseList
            else -> searchList
        }
        toolbarTitleLiveData.value = isWatchListMode
    }

    fun searchForResults() {
        if (isWatchListMode) {
            filterListByQuery()
            return
        }
        fetchSearchResult()
        resetPagination()
    }

    private fun filterListByQuery() {
        if (queryText.isEmpty()) {
            searchMutableLiveData.value = databaseList
            return
        }
        val queryList = databaseList.filter {
            it.title?.toLowerCase()?.contains(queryText.toLowerCase(), true) ?: false
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