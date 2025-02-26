package gr.pchasapis.moviedb.ui.fragment.theater

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.fragment.favourite.card.LoadingErrorCompose
import gr.pchasapis.moviedb.ui.fragment.favourite.screen.ToolbarView
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheatreScreen(
    uiState: TheaterUiState,
    modifier: Modifier = Modifier,
    nextScreen: (HomeDataModel) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            ToolbarView(
                text = stringResource(R.string.theatre_in_movies),
                scrollBehavior = scrollBehavior
            )
        },

        modifier = modifier.fillMaxSize(),
        content = { padding ->

            TheatreMainView(
                state = uiState,
                nextScreen = nextScreen,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }
    )
}

@Composable
private fun TheatreMainView(
    state: TheaterUiState,
    modifier: Modifier,
    nextScreen: (HomeDataModel) -> Unit
) {

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    ) {
        if (state.loading || state.list == null) {
            LoadingErrorCompose(shouldShowError = state.loading.not() && state.list == null)
        } else {
            Content(state.list, nextScreen)
        }
    }
}

@Composable
fun Content(
    list: Flow<PagingData<HomeDataModel>>,
    nextScreen: (HomeDataModel) -> Unit
) {


    val lazyPagingItems = list.collectAsLazyPagingItems()
    LazyVerticalGrid(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        columns = GridCells.Adaptive(100.dp)
    ) {
        val shape = RoundedCornerShape(10.dp)

        items(
            lazyPagingItems.itemCount,
            key = {
                lazyPagingItems[it]?.id!!
            }
        ) {
            val item = lazyPagingItems[it]!!
            AsyncImage(
                model = item.thumbnail,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .width(80.dp)
                    .clip(shape)
                    .shadow(elevation = 8.dp, shape = shape)
                    .clickable {
                        nextScreen(item)
                    },
                placeholder = painterResource(id = R.mipmap.ic_launcher),
                error = painterResource(id = R.mipmap.ic_launcher)
            )
        }

    }
}
