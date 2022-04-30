package gr.pchasapis.moviedb.ui.fragment.favourite.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.compose.PrimaryDark
import gr.pchasapis.moviedb.ui.fragment.details.ComposeText
import gr.pchasapis.moviedb.ui.fragment.details.MovieImage

@Composable
fun FavouriteRow(homeDataModel: HomeDataModel) {
    Card(
        backgroundColor = PrimaryDark,
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
    ) {
        FavouriteContent(homeDataModel = homeDataModel)
    }
}

@Composable
fun FavouriteContent(homeDataModel: HomeDataModel) {
    // var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {

        MovieImage(
            homeDataModel.thumbnail
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {

            ComposeText(
                text = homeDataModel.title ?: "-",
                maxLines = 2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp)
            )

            ComposeText(
                text = "${stringResource(R.string.home_release_data)}  ${homeDataModel.releaseDate}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

            ComposeText(
                text = "${stringResource(R.string.home_rating)}  ${homeDataModel.ratings}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun FavouriteList(messages: List<HomeDataModel>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        items(messages) { favourite ->
            FavouriteRow(homeDataModel = favourite)
        }
    }
}

@Composable
fun LoadingErrorCompose(shouldShowError: Boolean = false) {
    Surface(
        color = PrimaryDark,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (shouldShowError) {
                ComposeText(text = stringResource(id = R.string.favourite_empty_list))
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListPreview() {
    MovieDBTheme {
        val list = arrayListOf(
            HomeDataModel(
                ratings = "5",
                title = "Avengers",
                releaseDate = "25/5/2019"
            ),
            HomeDataModel(
                ratings = "5",
                title = "Avengers222",
                releaseDate = "25/5/2019"
            ),
            HomeDataModel(
                ratings = "5",
                title = "Avengers222",
                releaseDate = "25/5/2019"
            ),
            HomeDataModel(
                ratings = "5",
                title = "Avengers222",
                releaseDate = "25/5/2019"
            )
        )
        FavouriteList(list)
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    MovieDBTheme {
        FavouriteRow(
            HomeDataModel(
                ratings = "5",
                title = "Avengers",
                releaseDate = "25/5/2019"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    MovieDBTheme {
        LoadingErrorCompose()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    MovieDBTheme {
        LoadingErrorCompose(true)
    }
}

