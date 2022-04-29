package gr.pchasapis.moviedb.ui.fragment.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.databinding.FavouriteFragmentBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.compose.PrimaryDark
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteRow

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var binding: FavouriteFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {

            setContent {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                MovieDBTheme {

                    Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.primary) {

                        val viewModel = viewModel<FavouriteViewModel>()
                        val state = viewModel.state
                        val list = state.initialFavourite

                        if (state.loading) {
                            EmptyCompose()
                        }

                        if (list.isNotEmpty()) {
                            FavouriteList(messages = list)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FavouriteList(messages: List<HomeDataModel>) {
        LazyColumn {
            items(messages) { favourite ->
                FavouriteRow(homeDataModel = favourite)
            }
        }
    }

    @Composable
    fun EmptyCompose() {
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
