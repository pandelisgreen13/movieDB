@file:OptIn(ExperimentalMaterial3Api::class)

package gr.pchasapis.moviedb.ui.fragment.favourite.screen

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.fragment.favourite.FavouriteUiState
import gr.pchasapis.moviedb.ui.fragment.favourite.FavouriteViewModel
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteList
import gr.pchasapis.moviedb.ui.fragment.favourite.card.LoadingErrorCompose

@Composable
fun FavouriteRoute(
    viewModel: FavouriteViewModel = hiltViewModel(),
    nextScreen: (HomeDataModel) -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FavouriteScreen(state) {
        nextScreen(it)
    }
}

@Composable
fun FavouriteScreen(
    state: FavouriteUiState,
    action: (HomeDataModel) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())


    Scaffold(
        topBar = {
            ToolbarView(scrollBehavior)
        },

        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { it ->

            FavouriteMainView(
                state,
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(it)
                    .padding(top = it.calculateTopPadding())
            ) { model ->
                action.invoke(model)
            }
        }
    )
}


@Composable
private fun ToolbarView(scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.favourite_sceen)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun FavouriteMainView(
    state: FavouriteUiState,
    modifier: Modifier,
    onItemClicked: (HomeDataModel) -> Unit
) {

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    ) {
        if (state.loading || state.list.isEmpty()) {
            LoadingErrorCompose(shouldShowError = state.loading.not() && state.list.isEmpty())
        } else {
            FavouriteList(
                messages = state.list,
                onItemClicked = onItemClicked
            )
        }
    }
}