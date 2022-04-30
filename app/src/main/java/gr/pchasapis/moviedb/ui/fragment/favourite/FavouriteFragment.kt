package gr.pchasapis.moviedb.ui.fragment.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.databinding.FavouriteFragmentBinding
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteList
import gr.pchasapis.moviedb.ui.fragment.favourite.card.LoadingErrorCompose

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var binding: FavouriteFragmentBinding? = null

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
                        }
                    ) {
                        FavouriteMainView(state)
                    }
                }
            }
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
            }
        )
    }

    @Composable
    private fun FavouriteMainView(state: FavouriteUiState) {
        val list = state.initialFavourite

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.primary
        ) {
            if (state.loading || list.isEmpty()) {
                LoadingErrorCompose(shouldShowError = state.loading.not() && list.isEmpty())
            } else {
                FavouriteList(messages = list)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}