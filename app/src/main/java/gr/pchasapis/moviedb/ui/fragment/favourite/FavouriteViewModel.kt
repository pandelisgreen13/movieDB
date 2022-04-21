package gr.pchasapis.moviedb.ui.fragment.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.mvvm.interactor.favourite.FavouriteInteractorImp
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val favouriteInteractorImp:  FavouriteInteractorImp) : ViewModel() {

    fun readWatchListFromDatabase() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) { favouriteInteractorImp.fetchWatchListFromDatabase() }
//            response.data?.let {
//                watchListLiveData.value = it.isNotEmpty()
//                databaseList = it.toMutableList()
//                if (isWatchListMode && it.isNotEmpty()) {
//                    searchMutableLiveData.value = databaseList
//                } else {
//                    isWatchListMode = false
//                    searchMutableLiveData.value = searchList
//                }
//            } ?: response.throwable?.let {
//                Timber.e(it.toString())
//                isWatchListMode = false
//                searchMutableLiveData.value = searchList
//            } ?: run {
//                isWatchListMode = false
//                searchMutableLiveData.value = searchList
//            }
        }
    }
}
