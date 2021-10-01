package gr.pchasapis.moviedb.ui.activity.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.extensions.loadUrl
import gr.pchasapis.moviedb.databinding.ActivityDetailsBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.details.DetailsViewModel
import gr.pchasapis.moviedb.ui.activity.base.BaseActivity
import gr.pchasapis.moviedb.ui.activity.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel>() {

    val args : DetailsFragmentArgs by navArgs()

    private lateinit var binding: ActivityDetailsBinding

    @Inject
    lateinit var detailsInteractorImpl: DetailsInteractorImpl
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.trailerWebView.stopLoading()
        binding.trailerWebView.onPause()
    }

//    override fun onBackPressed() {
//        if (viewModel?.hasUserChangeFavourite == true && viewModel?.homeDataModel != null) {
//            val intent = Intent()
//            intent.putExtra(BUNDLE.MOVIE_DETAILS, viewModel?.homeDataModel)
//            setResult(RESULT_OK, intent)
//        } else {
//            setResult(RESULT_CANCELED)
//        }
//        finish()
//    }

    private fun initLayout() {
        binding.toolbarLayout.backButtonImageView.visibility = View.VISIBLE
        binding.toolbarLayout.backButtonImageView.setOnClickListener {  }
        binding.toolbarLayout.actionButtonImageView.setOnClickListener { viewModel?.toggleFavourite() }
    }

    private fun initViewModel() {
        viewModel = detailsViewModel
        val n = args.homeDataModel
        viewModel?.setUIModel(n) ?: kotlin.run {
            binding.emptyLayout?.root?.visibility = View.VISIBLE
        }
        initViewModelState(binding.loadingLayout, binding.emptyLayout)
        viewModel?.getDetailsList()?.observe(viewLifecycleOwner, { resultList ->
            resultList?.let {
                updateUi(it)
            } ?: run {
                binding.emptyLayout?.root?.visibility = View.VISIBLE
            }
        })

        viewModel?.getFavourite()?.observe(viewLifecycleOwner, { value ->
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
            trailerWebView.loadData(homeDataModel.videoUrl ?: "", "text/html", "utf-8")
        }
    }
}
