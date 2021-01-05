package gr.pchasapis.moviedb.ui.activity.base

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.databinding.LayoutEmptyBinding
import gr.pchasapis.moviedb.databinding.LayoutLoadingBinding
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModel

@SuppressLint("Registered")
open class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    private var materialDialog: MaterialDialog? = null
    protected var viewModel: T? = null

    override fun onDestroy() {
        super.onDestroy()
        materialDialog?.dismiss()
        materialDialog = null
    }

    fun initViewModelState(loadingLayout: LayoutLoadingBinding?, emptyLayout: LayoutEmptyBinding?) {
        viewModel?.showLoadingView()?.observe(this, Observer { value ->
            value?.let { show ->
                loadingLayout?.root?.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel?.showEmptyView()?.observe(this, Observer { value ->
            value?.let { show ->
                emptyLayout?.root?.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel?.showInternetError()?.observe(this, Observer { value ->
            value?.let { showError ->
                if (showError) {
                    showNoInternetError()
                }
            }
        })

        viewModel?.showGenericError()?.observe(this, Observer { value ->
            value?.let { showError ->
                if (showError) {
                    showGenericError()
                }
            }
        })
    }

    private fun showGenericError() {
        showErrorDialog(getString(R.string.generic_error), closeListener = { dialog ->
            dialog.dismiss()
            viewModel?.genericErrorLiveData?.value = false
        })
    }

    private fun showNoInternetError() {
        showErrorDialog(getString(R.string.no_internet), closeListener = { dialog ->
            dialog.dismiss()
            viewModel?.noInternetLiveData?.value = false
        })
    }

    fun showErrorDialog(errorDescription: String = "", closeListener: DialogCallback) {
        materialDialog = MaterialDialog(this).show {
            title(text = getString(R.string.dialog_title))
            message(text = errorDescription)
            positiveButton(text = getString(R.string.ok), click = closeListener)
        }
    }
}