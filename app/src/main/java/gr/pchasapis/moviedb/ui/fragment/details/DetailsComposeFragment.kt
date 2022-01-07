package gr.pchasapis.moviedb.ui.fragment.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.ACTIVITY_RESULT
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.mvvm.viewModel.details.compose.DetailsComposeViewModel
import gr.pchasapis.moviedb.mvvm.viewModel.details.compose.DetailsUiState
import gr.pchasapis.moviedb.ui.fragment.details.ui.theme.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.details.ui.theme.Primary
import gr.pchasapis.moviedb.ui.fragment.details.ui.theme.PrimaryDark

@AndroidEntryPoint
class DetailsComposeFragment : Fragment() {

    private val detailsViewModel: DetailsComposeViewModel by viewModels()

    private val args: DetailsComposeFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            detailsViewModel.setUIModel(args.homeDataModel)
            setContent {
                MovieDBTheme {
                    val uiState by detailsViewModel.uiState.collectAsState()
                    when (uiState) {
                        is DetailsUiState.Success -> {
                            val model = (uiState as DetailsUiState.Success).homeDataModel
                            SuccessCompose(model, detailsViewModel) {
                                onBackPressed()
                            }
                        }
                        is DetailsUiState.Loading -> {
                            LoadingCompose()
                        }
                        is DetailsUiState.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun onBackPressed() {
        if (detailsViewModel.hasUserChangeFavourite == true && detailsViewModel.homeDataModel != null) {
            val bundle = Bundle().apply {
                putParcelable(BUNDLE.MOVIE_DETAILS, detailsViewModel.homeDataModel)
            }
            setFragmentResult(ACTIVITY_RESULT.DETAILS, bundle)
        }
        findNavController().navigateUp()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingCompose() {
    Surface(
            color = PrimaryDark,
            modifier = Modifier.fillMaxSize()
    ) {
        Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SuccessCompose(homeDataModel: HomeDataModel? = null,
                           viewModel: DetailsComposeViewModel? = null,
                           onBackIconClicked: () -> Unit) {
    Surface(
            color = PrimaryDark,
            modifier = Modifier.fillMaxSize()
    ) {
        Column(
                modifier = Modifier.fillMaxWidth()
        ) {

            ToolbarCompose(homeDataModel, viewModel) {
                onBackIconClicked()
            }
            Spacer(modifier = Modifier.size(14.dp))
            ContentCompose(homeDataModel)
        }
    }
}

@Composable
fun ContentCompose(homeDataModel: HomeDataModel?) {

    Row {

        Image(
                painter = getImage(homeDataModel?.thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(120.dp)
        )

        Column(
                modifier = Modifier
                        .height(120.dp)
                        .padding(end = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            ComposeText(
                    text = homeDataModel?.summary ?: "-",
                    maxLines = 6,
                    modifier = Modifier.padding(bottom = 2.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ComposeText(
                        text = stringResource(R.string.details_genre),
                        modifier = Modifier.padding(bottom = 2.dp, end = 4.dp)
                )

                ComposeText(
                        text = homeDataModel?.genresName ?: "-",
                        maxLines = 2,
                        modifier = Modifier.padding(bottom = 2.dp)
                )

            }
        }

    }

}

@Composable
fun ToolbarCompose(homeDataModel: HomeDataModel?,
                   viewModel: DetailsComposeViewModel?,
                   onBackIconClicked: () -> Unit) {

    Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Primary)
    ) {

        ToolbarIcon(R.drawable.ic_arrow_back) {
            onBackIconClicked()
        }

        Text(
                text = homeDataModel?.title ?: "",
                color = Color.White,
                fontSize = 18.sp,
                maxLines = 1,
        )


        IconToggleButtonComposable(viewModel)

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieDBTheme {
        SuccessCompose {}
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


@Composable
private fun ToolbarIcon(drawable: Int,
                        onIconClicked: () -> Unit) {
    Icon(
            painter = painterResource(id = drawable),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        onIconClicked()
                    }
    )
}

@Composable
private fun IconToggleButtonComposable(viewModel: DetailsComposeViewModel?) {

    var checked by remember {
        mutableStateOf(viewModel?.showFavouriteLiveData?.value ?: false)
    }

    IconToggleButton(
            checked = checked,
            onCheckedChange = {
                viewModel?.toggleFavourite()
                checked = it
            }
    ) {
        val icon = if (checked) {
            R.drawable.ic_favourite_selected
        } else {
            R.drawable.ic_favourite_un_selected
        }

        Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                        .padding(10.dp)
        )
    }

}
