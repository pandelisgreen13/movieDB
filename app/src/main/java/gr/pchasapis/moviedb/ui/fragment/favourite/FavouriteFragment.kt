package gr.pchasapis.moviedb.ui.fragment.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.ActivityResult
import gr.pchasapis.moviedb.databinding.FavouriteFragmentBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteList
import gr.pchasapis.moviedb.ui.fragment.favourite.card.LoadingErrorCompose

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var binding: FavouriteFragmentBinding? = null


    private val viewModel: FavouriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentResult()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setContent {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                MovieDBTheme {

                    val viewModel = viewModel<FavouriteViewModel>()
                    val state = viewModel.state

                    Scaffold(
                        topBar = {
                            ToolbarView()
                        },
                        content = {

                            FavouriteMainView(
                                state,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .consumeWindowInsets(it)
                                    .padding(top = it.calculateTopPadding())
                            ) {
                                val action =
                                    FavouriteFragmentDirections.actionFavouriteFragmentToDetailsActivity(
                                        it
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun fragmentResult() {
        setFragmentResultListener(ActivityResult.DETAILS) { _: String, _: Bundle ->
            viewModel.readWatchListFromDatabase()
        }
    }

    @Composable
    private fun ToolbarView() {
        TopAppBar(
            title = { Text(stringResource(id = R.string.favourite_sceen)) },
            navigationIcon = {
                IconButton(onClick = {
                    findNavController().navigateUp()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White
            )
        )
    }

    @Composable
    private fun FavouriteMainView(
        state: FavouriteUiState,
        modifier: Modifier,
        onItemClicked: (HomeDataModel) -> Unit
    ) {
        val list = state.initialFavourite

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary
        ) {
            if (state.loading || list.isEmpty()) {
                LoadingErrorCompose(shouldShowError = state.loading.not() && list.isEmpty())
            } else {
                FavouriteList(
                    messages = list,
                    onItemClicked = onItemClicked
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}