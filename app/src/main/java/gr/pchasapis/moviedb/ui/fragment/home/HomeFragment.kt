package gr.pchasapis.moviedb.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.common.ActivityResult
import gr.pchasapis.moviedb.databinding.ActivityHomeBinding
import gr.pchasapis.moviedb.ui.base.BaseFragment
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeScreen


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>() {

    private val homeViewModel: HomeViewModel by viewModels()

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
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)


            setContent {
                MovieDBTheme {

                    val movies = homeViewModel.getMovies().collectAsLazyPagingItems()
                    HomeScreen(list = movies) {
                        homeViewModel.setQueryText(it)
                        homeViewModel.searchMovies()
                    }
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initViewModel(binding: ActivityHomeBinding) {


//        viewModel?.getMovieInTheatre()?.observe(viewLifecycleOwner) { value ->
//            value?.let { moviesInTheatre ->
//                val action = HomeFragmentDirections.actionHomeFragmentToTheatreFragment(
//                    TheatreDataModel(moviesInTheatre)
//                )
//                findNavController().navigate(action)
//            }
//        }
    }

    private fun initLayout() {
//        binding?.apply {
//            toolbarLayout.toolbarTitleTextView.text = getString(R.string.home_toolbar_title)
//
//            toolbarLayout.backButtonImageView.visibility = View.INVISIBLE
//            toolbarLayout.actionButtonImageView.visibility = View.VISIBLE
//            toolbarLayout.actionButtonImageView.setImageDrawable(activity?.let {
//                ContextCompat.getDrawable(
//                    it,
//                    R.drawable.ic_theatre
//                )
//            })
//
//            toolbarLayout.actionButtonImageView.setOnClickListener {
//                viewModel?.fetchMovieInTheatre()
//            }
//
//            searchImageButton.setOnClickListener {
//                if (searchEditText.text.toString().isEmpty()) {
//                    showErrorDialog(
//                        getString(R.string.home_empty_field),
//                        closeListener = { dialog ->
//                            dialog.dismiss()
//                            viewModel?.genericErrorLiveData?.value = false
//                        })
//                }
//                handleClickSearch()
//            }
//            watchListButton.setOnClickListener {
//                findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
//            }
//
//            searchEditText.addTextChangedListener(object : TextWatcher {
//                override fun onTextChanged(
//                    charSequence: CharSequence,
//                    arg1: Int,
//                    arg2: Int,
//                    arg3: Int
//                ) {
//                    viewModel?.setQueryText(searchEditText.text?.trim().toString())
//                }
//
//                override fun beforeTextChanged(
//                    arg0: CharSequence,
//                    arg1: Int,
//                    arg2: Int,
//                    arg3: Int
//                ) {
//                }
//
//                override fun afterTextChanged(arg0: Editable) {}
//            })
//
//            searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
//                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
//                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                        handleClickSearch()
//                        return true
//                    }
//                    return false
//                }
//            })
//
//            val linearLayoutManager = LinearLayoutManager(activity)
//            binding?.recyclerViewLayout?.homeRecyclerView?.layoutManager = linearLayoutManager
//            homeRecyclerViewAdapter = HomeRecyclerViewAdapter(
//                onItemClicked = { homeDataModel ->
////                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(homeDataModel)
//                    homeDataModel?.let {
//                        val action =
//                            HomeFragmentDirections.actionHomeFragmentToDetailsComposeFragment2(
//                                homeDataModel
//                            )
//                        findNavController().navigate(action)
//                    }
//                })
//
//            binding?.recyclerViewLayout?.homeRecyclerView?.adapter = homeRecyclerViewAdapter
//        }
    }

    private fun handleClickSearch() {
//        binding?.recyclerViewLayout?.homeRecyclerView?.smoothScrollToPosition(Definitions.FIRST_POSITION)
//        binding?.searchEditText?.clearFocus()
//        activity?.let { closeSoftKeyboard(it) }
//        viewModel?.searchMovies()
    }

    private fun fragmentResult() {
        setFragmentResultListener(ActivityResult.DETAILS) { _: String, bundle: Bundle ->
            // remove it
        }
    }
}
