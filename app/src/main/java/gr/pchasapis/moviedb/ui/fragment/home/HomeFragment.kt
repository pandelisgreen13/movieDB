package gr.pchasapis.moviedb.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.ui.base.BaseFragment
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeScreen


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>() {

    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)


            setContent {
                MovieDBTheme {

                    val movies = homeViewModel.getMovies()
                    HomeScreen(
                        flow = movies,
                        textChanged = {
                            homeViewModel.setQueryText(it)
                            homeViewModel.searchMovies()
                        },
                        onItemClicked = {
                            val action =
                                HomeFragmentDirections.actionHomeFragmentToDetailsActivity(it)
                            findNavController().navigate(action)
                        }
                    )
                }
            }
        }
    }


}
