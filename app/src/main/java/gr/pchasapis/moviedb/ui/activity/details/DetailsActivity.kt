package gr.pchasapis.moviedb.ui.activity.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.application.MovieApplication
import gr.pchasapis.moviedb.common.extensions.loadUrl
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModelFactory
import gr.pchasapis.moviedb.mvvm.viewModel.details.DetailsViewModel
import gr.pchasapis.moviedb.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class DetailsActivity : BaseActivity<DetailsViewModel>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initLayout()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        trailerWebView?.stopLoading()
        trailerWebView?.onPause()
    }

    override fun onBackPressed() {
        if (viewModel?.hasUserChangeFavourite == true && viewModel?.homeDataModel != null) {
            val intent = Intent()
            intent.putExtra(BUNDLE.MOVIE_DETAILS, viewModel?.homeDataModel)
            setResult(RESULT_OK, intent)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    private fun initLayout() {
        backButtonImageView.visibility = View.VISIBLE
        backButtonImageView.setOnClickListener { onBackPressed() }
        actionButtonImageView.setOnClickListener { viewModel?.toggleFavourite() }
    }

    private fun initViewModel() {
        val viewModelFactory = BaseViewModelFactory {
            DetailsViewModel(
                    DetailsInteractorImpl(MovieApplication.get()?.movieClient!!, MovieDbDatabase.get(this)),
                    intent?.extras?.getParcelable(BUNDLE.MOVIE_DETAILS))
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
        initViewModelState()
        viewModel?.getDetailsList()?.observe(this, Observer { resultList ->
            resultList?.let {
                updateUi(it)
            } ?: run { emptyView.visibility = View.VISIBLE }
        })

        viewModel?.getFavourite()?.observe(this, Observer { value ->
            value?.let { isFavourite ->
                actionButtonImageView.isSelected = isFavourite
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun updateUi(homeDataModel: HomeDataModel) {
        actionButtonImageView.visibility = View.VISIBLE
        thumbnailImageView.loadUrl(homeDataModel.thumbnail)
        toolbarTitleTextView.text = homeDataModel.title
        summaryTextView.text = homeDataModel.summary
        genreTextView.text = homeDataModel.genresName
        actionButtonImageView.isSelected = homeDataModel.isFavorite

        trailerWebView.settings.javaScriptEnabled = true

        trailerWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
        val webSettings = trailerWebView.settings
        webSettings.javaScriptEnabled = true
        trailerWebView.loadData(homeDataModel.videoUrl, "text/html", "utf-8")
    }
}
