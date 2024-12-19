package gr.pchasapis.moviedb.ui.fragment.theater

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.ui.fragment.favourite.card.LoadingErrorCompose
import gr.pchasapis.moviedb.ui.fragment.favourite.screen.ToolbarView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheatreScreen(
    uiState: TheaterUiState,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            ToolbarView(text = "Movies in theatre")
        },

        modifier = modifier.fillMaxSize(),
        content = { padding ->
            TheatreMainView(
                state = uiState,
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(padding)
                    .padding(top = padding.calculateTopPadding())
            )
        }
    )
}

@Composable
private fun TheatreMainView(
    state: TheaterUiState,
    modifier: Modifier
) {

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    ) {
        if (state.loading || state.list.isEmpty()) {
            LoadingErrorCompose(shouldShowError = state.loading.not() && state.list.isEmpty())
        } else {
            Content(state.list)
        }
    }
}

@Composable
fun Content(list: List<MovieDataModel>) {


    LazyVerticalGrid(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        columns = GridCells.Adaptive(100.dp)
    ) {
        items(list, key = { it.id!! }) { item ->

            AsyncImage(
                model = item.thumbnail,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                placeholder = painterResource(id = R.mipmap.ic_launcher),
                error = painterResource(id = R.mipmap.ic_launcher)
            )
        }

    }
}
