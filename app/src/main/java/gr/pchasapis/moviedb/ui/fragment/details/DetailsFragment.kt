package gr.pchasapis.moviedb.ui.fragment.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.ACTIVITY_RESULT
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.databinding.ActivityDetailsBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.interactor.details.DetailsInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.details.DetailsViewModel
import gr.pchasapis.moviedb.ui.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel>() {

    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var binding: ActivityDetailsBinding

    @Inject
    lateinit var detailsInteractorImpl: DetailsInteractorImpl
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initViewModel()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.trailerWebView.stopLoading()
        binding.trailerWebView.onPause()
    }

    private fun onBackPressed() {
        if (viewModel?.hasUserChangeFavourite == true && viewModel?.homeDataModel != null) {
            val bundle = Bundle().apply {
                putParcelable(BUNDLE.MOVIE_DETAILS, viewModel?.homeDataModel)
            }
            setFragmentResult(ACTIVITY_RESULT.DETAILS, bundle)
        }
        findNavController().navigateUp()
    }

    private fun initLayout() = with(binding.toolbarLayout) {
        backButtonImageView.visibility = View.VISIBLE
        backButtonImageView.setOnClickListener {
            onBackPressed()
        }
        actionButtonImageView.setOnClickListener { viewModel?.toggleFavourite() }
    }

    private fun initViewModel() {
        viewModel = detailsViewModel
        viewModel?.setUIModel(args.homeDataModel) ?: kotlin.run {
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

            thumbnailImageView.setContent {
                MdcTheme {
                    Image(
                            painter = getImage(homeDataModel.thumbnail),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                    )
                }
            }

            toolbarLayout.toolbarTitleTextView.text = homeDataModel.title
            summaryTextView.setContent {
                MdcTheme {
                    ComposeText(homeDataModel.summary ?: "-", 6, Modifier.padding(bottom = 2.dp))
                }
            }
            genreTitleTextView.setContent {
                MdcTheme {
                    ComposeText(
                            text = stringResource(R.string.details_genre),
                            modifier = Modifier.padding(bottom = 2.dp, end = 4.dp))
                }
            }

            genreTextView.setContent {
                MdcTheme {
                    ComposeText(
                            text = homeDataModel.genresName ?: "-",
                            maxLines = 2,
                            modifier = Modifier.padding(bottom = 2.dp))
                }
            }

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

    @Composable
    private fun getImage(thumbnail: String?): Painter {
        return if (thumbnail != null && thumbnail.contains("null")) {
            painterResource(id = R.mipmap.ic_launcher)
        } else {
            rememberImagePainter(thumbnail)
        }
    }

    @Composable
    private fun ComposeText(text: String = "-",
                            maxLines: Int = 1,
                            modifier: Modifier) {
        Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
        )
    }
}
