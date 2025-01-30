package gr.pchasapis.moviedb.ui.fragment.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.favourite.FavouriteInteractorImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val favouriteInteractorImpl: FavouriteInteractorImpl) :
    ViewModel() {


    private val _uiState = MutableStateFlow(FavouriteUiState())

    val uiState: StateFlow<FavouriteUiState> = _uiState

    val response = favouriteInteractorImpl.fetchWatchListFromDatabase().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    init {
        readWatchListFromDatabase()
    }

    fun readWatchListFromDatabase() {

        viewModelScope.launch {
            favouriteInteractorImpl.fetchWatchListFromDatabase().collectLatest { favourites ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        list = favourites
                    )
                }
            }
        }
    }

    fun filterBy(favouriteFilterEvents: FavouriteFilterEvents) {
        val filteredList = when (favouriteFilterEvents) {
            FavouriteFilterEvents.ByDateAdded -> {
                uiState.value.list.sortedByDescending { it.dateAdded }
            }

            FavouriteFilterEvents.ByName -> {
                uiState.value.list.sortedBy { it.title }
            }

            FavouriteFilterEvents.ByRate -> {
                uiState.value.list.sortedByDescending { it.ratings?.toDouble() ?: 0.0 }
            }
        }
        _uiState.update {
            it.copy(
                list = filteredList,
                filterState= favouriteFilterEvents
            )
        }
    }
}


data class FavouriteUiState(
    val list: List<HomeDataModel> = listOf(),
    val filterState: FavouriteFilterEvents = FavouriteFilterEvents.ByDateAdded,
    val loading: Boolean = true
)

sealed class FavouriteFilterEvents {
    data object ByDateAdded : FavouriteFilterEvents()
    data object ByRate : FavouriteFilterEvents()
    data object ByName : FavouriteFilterEvents()

}