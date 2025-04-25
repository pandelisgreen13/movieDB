@file:OptIn(ExperimentalMaterial3Api::class)

package gr.pchasapis.moviedb.ui.fragment.favourite.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.ColorAccent
import gr.pchasapis.moviedb.ui.fragment.favourite.FavouriteFilterEvents
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

    FavouriteScreen(
        state = state,
        action = {
            nextScreen(it)
        },
        chipAction = {
            viewModel.filterBy(it)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavouriteScreen(
    state: FavouriteUiState,
    action: (HomeDataModel) -> Unit,
    chipAction: (FavouriteFilterEvents) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())


    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Column {
                ToolbarView(
                    scrollBehavior = scrollBehavior,
                    text = stringResource(id = R.string.favourite_screen)
                )

                FilterView(state, chipAction)
            }
        },

        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { innerPadding ->

            FavouriteMainView(
                state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { model ->
                action.invoke(model)
            }
        }
    )
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FilterView(
    state: FavouriteUiState,
    chipAction: (FavouriteFilterEvents) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Chip(
            text = stringResource(R.string.filter_date),
            isSelected = state.filterState is FavouriteFilterEvents.ByDateAdded
        ) {
            chipAction.invoke(FavouriteFilterEvents.ByDateAdded)
        }
        Chip(
            stringResource(R.string.filter_rate),
            isSelected = state.filterState is FavouriteFilterEvents.ByRate
        ) {
            chipAction.invoke(FavouriteFilterEvents.ByRate)
        }
        Chip(
            stringResource(R.string.filter_name),
            isSelected = state.filterState is FavouriteFilterEvents.ByName
        ) {
            chipAction.invoke(FavouriteFilterEvents.ByName)
        }
    }
}

@Composable
fun Chip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        colors = FilterChipDefaults.filterChipColors().copy(
            selectedContainerColor = ColorAccent
        ),
        modifier = modifier.padding(horizontal = 2.dp),
        selected = isSelected,
        label = {
            Text(
                text = text,
                color = Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    )
}


@Composable
fun ToolbarView(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    text: String
) {
    CenterAlignedTopAppBar(
        title = { Text(text) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = scrollBehavior,
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