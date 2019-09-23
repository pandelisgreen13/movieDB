package gr.pchasapis.moviedb.mvvm.viewModel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    private val viewModelJob = SupervisorJob()
    protected val bgDispatcher: CoroutineDispatcher = Dispatchers.IO
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    protected var loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    protected var emptyLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var noInternetLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var genericErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     *
     * This would ensure that our coroutines do not get canceled even
     * when the screen rotates and our activity is recreated
     */

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showEmptyView(): LiveData<Boolean> {
        return emptyLiveData
    }

    fun showLoadingView(): LiveData<Boolean> {
        return loadingLiveData
    }

    fun showInternetError(): LiveData<Boolean> {
        return noInternetLiveData
    }

    fun showGenericError(): LiveData<Boolean> {
        return genericErrorLiveData
    }

    fun onErrorThrowable(throwable: Throwable?, isShowEmptyView: Boolean = false) {
        loadingLiveData.value = false
        if (throwable == null) {
            genericErrorLiveData.value = true
            return
        }
        if (isNetworkError(throwable)) {
            noInternetLiveData.value = true
        } else {
            genericErrorLiveData.value = true
        }
        if (isShowEmptyView)
            emptyLiveData.value = true
    }

    private fun isNetworkError(throwable: Throwable): Boolean {
        when (throwable) {
            is UnknownHostException -> {
                return true
            }
        }
        return false
    }
}