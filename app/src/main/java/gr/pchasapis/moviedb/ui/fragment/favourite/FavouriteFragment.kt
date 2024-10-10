package gr.pchasapis.moviedb.ui.fragment.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.common.ActivityResult
import gr.pchasapis.moviedb.databinding.FavouriteFragmentBinding
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.favourite.screen.FavouriteScreen

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
                    val state by viewModel.uiState.collectAsStateWithLifecycle()

//                    FavouriteScreen(state){
//                        val action =
//                            FavouriteFragmentDirections.actionFavouriteFragmentToDetailsActivity(
//                                it
//                            )
//                        findNavController().navigate(action)
//                    }
                }
            }
        }
    }

    private fun fragmentResult() {
        setFragmentResultListener(ActivityResult.DETAILS) { _: String, _: Bundle ->
            viewModel.readWatchListFromDatabase()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}