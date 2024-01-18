@file:OptIn(ExperimentalMaterial3Api::class)

package gr.pchasapis.moviedb.ui.fragment.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteRow
import gr.pchasapis.moviedb.ui.fragment.home.HomeFragmentDirections
import gr.pchasapis.moviedb.ui.fragment.home.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

@Composable
fun HomeRoute(homeViewModel: HomeViewModel = hiltViewModel()) {

    val movies = homeViewModel.getMovies()
    HomeScreen(
        flow = movies,
        textChanged = {
            homeViewModel.setQueryText(it)
            homeViewModel.searchMovies()
        },
        onItemClicked = {
//            val action =
//                HomeFragmentDirections.actionHomeFragmentToDetailsActivity(it)
//            findNavController().navigate(action)
        }
    )
}

@Composable
fun HomeScreen(
    flow: Flow<PagingData<HomeDataModel>>,
    textChanged: (String) -> Unit = {},
    onItemClicked: (HomeDataModel) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val lazyPagingItems = flow.collectAsLazyPagingItems()

        ToolbarCenterAligned {

        }
        var text by remember { mutableStateOf("") }

        SearchView(
            text = text,
            placeHolder = "Search it"
        ) {
            text = it

        }
        LaunchedEffect(key1 = text) {
            if (text.isBlank()) return@LaunchedEffect
            delay(2000)
            textChanged(text.trim())
        }

        HomeList(
            messages = lazyPagingItems,
            onItemClicked = onItemClicked
        )

    }

}

@Composable
fun HomeList(messages: LazyPagingItems<HomeDataModel>, onItemClicked: (HomeDataModel) -> Unit) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
    ) {
        items(messages.itemCount) {
            val favourite = messages[it]!!
            FavouriteRow(homeDataModel = favourite) { model ->
                onItemClicked(model)
            }
        }

        when {
            messages.loadState.append is LoadState.Loading -> {
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

            messages.itemSnapshotList.size == 0 && messages.loadState.refresh is LoadState.Loading -> {
                item {
                    CircularProgressIndicator(color = Color.White)

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
                ratings = "5", title = "Avengers", releaseDate = "25/5/2019"
            ), HomeDataModel(
                ratings = "5", title = "Avengers222", releaseDate = "25/5/2019"
            ), HomeDataModel(
                ratings = "5", title = "Avengers222", releaseDate = "25/5/2019"
            ), HomeDataModel(
                ratings = "5", title = "Avengers222", releaseDate = "25/5/2019"
            )
        )
        val pagingData = PagingData.from(list)
// pass pagingData containing fake data to a MutableStateFlow
        val fakeDataFlow = MutableStateFlow(pagingData)
        HomeScreen(flow = fakeDataFlow)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolbarCenterAligned(onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.home_toolbar_title)) },
        actions = {
            IconButton(onClick = {
                onClick.invoke()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_theatre), contentDescription = null
                )
            }
        },
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
    textChange: (String) -> Unit
) {

    TextField(
        value = text,
        onValueChange = textChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
        ),
        placeholder = {
            Text(text = placeHolder)
        },
        trailingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null)
        },
    )
}

@Preview
@Composable
fun PreviewToolbar() {
    MovieDBTheme {
        ToolbarCenterAligned({})
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