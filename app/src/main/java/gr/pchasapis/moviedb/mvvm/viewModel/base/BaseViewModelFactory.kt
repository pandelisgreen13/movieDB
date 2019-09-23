package gr.pchasapis.moviedb.mvvm.viewModel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class BaseViewModelFactory< T>(val creator: () -> T) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}