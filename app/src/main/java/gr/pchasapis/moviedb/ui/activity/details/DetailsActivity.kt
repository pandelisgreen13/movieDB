package gr.pchasapis.moviedb.ui.activity.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.application.MovieApplication
import gr.pchasapis.moviedb.common.extensions.loadUrl
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.databinding.ActivityDetailsBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModelFactory
import gr.pchasapis.moviedb.mvvm.viewModel.details.DetailsViewModel
import gr.pchasapis.moviedb.ui.activity.base.BaseActivity


class DetailsActivity : BaseActivity<DetailsViewModel>() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.trailerWebView.stopLoading()
        binding.trailerWebView.onPause()
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
        binding.toolbarLayout.backButtonImageView.visibility = View.VISIBLE
        binding.toolbarLayout.backButtonImageView.setOnClickListener { onBackPressed() }
        binding.toolbarLayout.actionButtonImageView.setOnClickListener { viewModel?.toggleFavourite() }
    }

    private fun initViewModel() {
        val viewModelFactory = BaseViewModelFactory {
            DetailsViewModel(
                    DetailsInteractorImpl(MovieApplication.get()?.movieClient!!, MovieDbDatabase.get(this)),
                    intent?.extras?.getParcelable(BUNDLE.MOVIE_DETAILS))
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
        initViewModelState(binding.loadingLayout, binding.emptyLayout)
        viewModel?.getDetailsList()?.observe(this, { resultList ->
            resultList?.let {
                updateUi(it)
            } ?: run {
                binding.emptyLayout?.root?.visibility = View.VISIBLE
            }
        })

        viewModel?.getFavourite()?.observe(this, { value ->
            value?.let { isFavourite ->
                binding.toolbarLayout.actionButtonImageView.isSelected = isFavourite
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun updateUi(homeDataModel: HomeDataModel) {
        binding.apply {
            toolbarLayout.actionButtonImageView.visibility = View.VISIBLE
            thumbnailImageView.loadUrl(homeDataModel.thumbnail)
            toolbarLayout.toolbarTitleTextView.text = homeDataModel.title
            summaryTextView.text = homeDataModel.summary
            genreTextView.text = homeDataModel.genresName
            toolbarLayout.actionButtonImageView.isSelected = homeDataModel.isFavorite

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
}
