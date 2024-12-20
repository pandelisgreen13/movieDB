package gr.pchasapis.moviedb.ui.fragment.details

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.SimilarMoviesModel
import gr.pchasapis.moviedb.mvvm.viewModel.details.compose.DetailsComposeViewModel
import gr.pchasapis.moviedb.mvvm.viewModel.details.compose.DetailsUiState
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.compose.Primary
import gr.pchasapis.moviedb.ui.compose.PrimaryDark

@Composable
fun DetailsRoute(
    detailsViewModel: DetailsComposeViewModel = hiltViewModel(),
    passData: HomeDataModel?,
    onBackIconClicked: () -> Unit
) {
    detailsViewModel.setUIModel(passData)

    val uiState by detailsViewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is DetailsUiState.Success -> {
            val model = (uiState as DetailsUiState.Success)
            Details(model, detailsViewModel) {
                onBackIconClicked()
            }
        }

        is DetailsUiState.Loading -> {
            LoadingCompose()
        }

        is DetailsUiState.Error -> {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingCompose() {
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
private fun Details(
    homeDataModel: DetailsUiState.Success,
    viewModel: DetailsComposeViewModel? = null,
    onBackIconClicked: () -> Unit
) {
    Surface(
        color = PrimaryDark,
        modifier = Modifier.fillMaxSize(),
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            ToolbarCompose(
                title = "",
                toolbarColor = Color.Transparent,
                isFavourite = viewModel?.showFavouriteLiveData?.value ?: false,
                onBackIconClicked = {
                    onBackIconClicked()
                },
                onFavouriteIconClicked = {
                    viewModel?.toggleFavourite()
                })

            var cardFace by rememberSaveable {
                mutableStateOf(CardFace.Front)
            }

            FlipCard(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                cardFace = cardFace,
                front = {
                    AsyncImage(
                        model = homeDataModel.homeDataModel.thumbnail,
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = it
                            .size(300.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        placeholder = painterResource(id = R.mipmap.ic_launcher)
                    )
                },
                back = {
                    BackCard(it = it, homeDataModel = homeDataModel)
                }
            ) {
                cardFace = cardFace.next
            }

            ComposeText(
                text = homeDataModel.homeDataModel.title ?: "-",
                maxLines = 2,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            ComposeText(
                text = homeDataModel.homeDataModel.summary ?: "-",
                maxLines = 6,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            ComposeText(
                text = stringResource(R.string.similar),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            if (homeDataModel.similarMovies.isEmpty()) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 40.dp)
                )
            } else {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(homeDataModel.similarMovies, key = { it.id }) { item ->
                        AsyncImage(
                            model = item.image,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(150.dp)
                                .width(80.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            placeholder = painterResource(id = R.mipmap.ic_launcher),
                            error = painterResource(id = R.mipmap.ic_launcher)
                        )
                    }
                }
            }

            Spacer(Modifier.height(50.dp))

        }
    }
}

@Composable
private fun BackCard(
    it: Modifier,
    homeDataModel: DetailsUiState.Success
) {
    Card(
        modifier = it.size(300.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {

            Text(
                stringResource(R.string.details_genre),
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 5.dp),
                color = Color.White
            )
            Text(
                homeDataModel.homeDataModel.genresName.orEmpty(),
                modifier = Modifier.padding(top = 5.dp),
                color = Color.White
            )
            Text(
                stringResource(R.string.home_rating),
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 15.dp),
                color = Color.White
            )
            Text(
                homeDataModel.homeDataModel.ratings.orEmpty(),
                modifier = Modifier.padding(top = 5.dp),
                color = Color.White
            )
            Text(
                stringResource(R.string.home_release_data),
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 15.dp),
                color = Color.White
            )
            Text(
                homeDataModel.homeDataModel.releaseDate.orEmpty(),
                modifier = Modifier.padding(top = 5.dp),
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Test() {
    MovieImage(null)
}

@Composable
fun MovieImage(
    thumbnail: String?,
    size: Dp = 120.dp
) {
    AsyncImage(
        model = thumbnail, contentDescription = "", contentScale = ContentScale.Crop,
        modifier = Modifier.size(size),
        placeholder = painterResource(id = R.mipmap.ic_launcher)
    )
}

@Composable
fun ToolbarCompose(
    title: String,
    modifier: Modifier = Modifier,
    toolbarColor: Color = Primary,
    isFavourite: Boolean = false,
    onBackIconClicked: () -> Unit,
    onFavouriteIconClicked: (() -> Unit)? = null
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(toolbarColor)
    ) {

        ToolbarIcon {
            onBackIconClicked()
        }

        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            maxLines = 1,
        )

        IconToggleButtonComposable(isFavourite) {
            onFavouriteIconClicked?.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieDBTheme {
        Details(
            DetailsUiState.Success(
                HomeDataModel(),
                listOf(
                    SimilarMoviesModel(),
                    SimilarMoviesModel(),
                    SimilarMoviesModel(),
                )
            )
        ) {}
    }
}


@Composable
fun ComposeText(
    text: String = "-",
    fontSize: TextUnit = 12.sp,
    maxLines: Int = 1,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        fontWeight = fontWeight
    )
}


@Composable
private fun ToolbarIcon(
    drawable: Int = R.drawable.ic_arrow_back,
    onIconClicked: () -> Unit
) {
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
private fun IconToggleButtonComposable(
    showFavouriteLiveData: Boolean = false,
    onCheckedChange: () -> Unit
) {

    var checked by remember {
        mutableStateOf(showFavouriteLiveData)
    }

    IconToggleButton(
        checked = checked,
        onCheckedChange = {
            onCheckedChange.invoke()
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

@Composable
fun FlipCard(
    cardFace: CardFace,
    front: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier,
    back: @Composable (Modifier) -> Unit,
    onClick: (CardFace) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 1800,
            easing = FastOutSlowInEasing,
        ), label = ""
    )

    val mod = modifier
        .graphicsLayer {
            rotationY = rotation
            cameraDistance = 12f * density
        }
        .clickable {
            onClick(cardFace)
        }

    if (rotation <= 90f) {
        front(mod)
    } else {
        back(mod.graphicsLayer(rotationY = 180f))
    }
}

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },

    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}
