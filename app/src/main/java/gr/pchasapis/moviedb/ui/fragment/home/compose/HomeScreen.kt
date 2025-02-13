package gr.pchasapis.moviedb.ui.fragment.home.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.compose.PrimaryDark
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteRow
import gr.pchasapis.moviedb.ui.fragment.favourite.screen.ToolbarView
import gr.pchasapis.moviedb.ui.fragment.home.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    textChanged: (String) -> Unit = {},
    onItemClicked: (HomeDataModel) -> Unit = {}
) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        modifier = Modifier
            .consumeWindowInsets(PaddingValues(bottom = 100.dp))
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
            ) {
                ToolbarView(
                    scrollBehavior = scrollBehavior,
                    text = stringResource(id = R.string.home_toolbar_title)
                )

                var text by rememberSaveable { mutableStateOf("") }

                SearchView(
                    text = text,
                    placeHolder = "Search it"
                ) {
                    text = it

                }
                LaunchedEffect(key1 = text) {
                    if (text.isBlank()) return@LaunchedEffect
                    delay(1000)
                    textChanged(text.trim())
                }
            }
        }
    ) { inner ->
        Column(
            Modifier.padding(inner)
        ) {


            if (state.isLoading) {
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator(
                    color = PrimaryDark,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            }

            state.data?.let {
                HomeList(
                    messages = it,
                    onItemClicked = onItemClicked,
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)

                )
            }


        }
    }


}

@Composable
fun HomeList(
    messages: Flow<PagingData<HomeDataModel>>,
    onItemClicked: (HomeDataModel) -> Unit,
    modifier: Modifier = Modifier
) {

    val lazyPagingItems = messages.collectAsLazyPagingItems()

    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        columns = GridCells.Adaptive(300.dp),
        modifier = modifier
    ) {
        items(
            lazyPagingItems.itemCount,
            key = {
                lazyPagingItems[it]?.id!!
            }
        ) {
            val favourite = lazyPagingItems[it]!!
            FavouriteRow(
                homeDataModel = favourite,
                modifier = Modifier.animateItem()
            ) { model ->
                onItemClicked(model)
            }
        }


        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Pagination Loading")

                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "initial Error")

                }
            }
        }
        if (lazyPagingItems.loadState.append is LoadState.Error) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Pagination Error")

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    MovieDBTheme {
        val list = arrayListOf(
            HomeDataModel(
                ratings = "5", title = "Avengers", releaseDate = "25/5/2019", id = 5
            ), HomeDataModel(
                ratings = "5", title = "Avengers222", releaseDate = "25/5/2019", id = 6
            ), HomeDataModel(
                ratings = "5", title = "Avengers222", releaseDate = "25/5/2019", id = 7
            ), HomeDataModel(
                ratings = "5", title = "Avengers222", releaseDate = "25/5/2019", id = 8
            )
        )
        val pagingData = PagingData.from(list)
// pass pagingData containing fake data to a MutableStateFlow
        val fakeDataFlow = MutableStateFlow(pagingData)
        HomeScreen(state = HomeUiState(data = fakeDataFlow))
    }
}

@Preview
@Composable
fun PreviewLoadingHome() {
    MovieDBTheme {

        HomeScreen(state = HomeUiState(isLoading = true))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolbarCenterAligned() {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.home_toolbar_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun SearchView(
    text: String,
    placeHolder: String = "",
    modifier: Modifier = Modifier,
    textChange: (String) -> Unit
) {

    TextField(
        value = text,
        onValueChange = textChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            selectionColors = TextSelectionColors(
                handleColor = colorResource(R.color.colorAccent),
                backgroundColor = Color(0xFF4286F4).copy(alpha = 0.4f)
            )
        ),
        placeholder = {
            Text(text = placeHolder, color = Color.White.copy(alpha = 0.2f))
        },
        trailingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
        },
    )
}

@Preview
@Composable
fun PreviewToolbar() {
    MovieDBTheme {
        ToolbarCenterAligned()
    }
}

@Preview
@Composable
fun PreviewSearch() {
    MovieDBTheme {
        SearchView("search...") {

        }
    }
}