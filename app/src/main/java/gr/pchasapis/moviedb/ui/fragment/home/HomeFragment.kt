package gr.pchasapis.moviedb.ui.fragment.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import closeSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.ACTIVITY_RESULT
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.databinding.ActivityHomeBinding
import gr.pchasapis.moviedb.model.data.TheatreDataModel
import gr.pchasapis.moviedb.mvvm.viewModel.home.HomeViewModel
import gr.pchasapis.moviedb.ui.adapter.home.HomeRecyclerViewAdapter
import gr.pchasapis.moviedb.ui.base.BaseFragment
import gr.pchasapis.moviedb.ui.custom.pagination.PaginationScrollListener
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var homeRecyclerViewAdapter: HomeRecyclerViewAdapter? = null
    private var paginationScrollListener: PaginationScrollListener? = null
    private var binding: ActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentResult()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        binding?.let {
            initViewModel(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initViewModel(binding: ActivityHomeBinding) {
        viewModel = homeViewModel
        initViewModelState(binding.loadingLayout, binding.emptyLayout)
        viewModel?.getSearchList()?.observe(viewLifecycleOwner) { resultList ->
            resultList?.let {
                homeRecyclerViewAdapter?.setSearchList(it)
            } ?: run {
                binding.emptyLayout.root.visibility = View.VISIBLE
            }
        }

        viewModel?.getPaginationStatus()?.observe(viewLifecycleOwner) { value ->
            value?.let { isPaginationFinished ->
                paginationScrollListener?.finishedPagination(isPaginationFinished)
            }
        }

        viewModel?.getPaginationLoader()?.observe(viewLifecycleOwner) { value ->
            value?.let { show ->
                binding.recyclerViewLayout.moreProgressView.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        viewModel?.getMovieInTheatre()?.observe(viewLifecycleOwner) { value ->
            value?.let { moviesInTheatre ->
                val action = HomeFragmentDirections.actionHomeFragmentToTheatreFragment(TheatreDataModel(moviesInTheatre))
                findNavController().navigate(action)
            }
        }
    }

    private fun initLayout() {
        binding?.apply {
            toolbarLayout.toolbarTitleTextView.text = getString(R.string.home_toolbar_title)

            toolbarLayout.backButtonImageView.visibility = View.INVISIBLE
            toolbarLayout.actionButtonImageView.visibility = View.VISIBLE
            toolbarLayout.actionButtonImageView.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_theatre) })

            toolbarLayout.actionButtonImageView.setOnClickListener {
                viewModel?.fetchMovieInTheatre()
            }

            searchImageButton.setOnClickListener {
                if (searchEditText.text.toString().isEmpty()) {
                    showErrorDialog(getString(R.string.home_empty_field), closeListener = { dialog ->
                        dialog.dismiss()
                        viewModel?.genericErrorLiveData?.value = false
                    })
                }
                handleClickSearch()
            }
            watchListButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
            }

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(charSequence: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                    viewModel?.setQueryText(searchEditText.text?.trim().toString())
                }

                override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
                override fun afterTextChanged(arg0: Editable) {}
            })

            searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        handleClickSearch()
                        return true
                    }
                    return false
                }
            })

            val linearLayoutManager = LinearLayoutManager(activity)
            binding?.recyclerViewLayout?.homeRecyclerView?.layoutManager = linearLayoutManager
            homeRecyclerViewAdapter = HomeRecyclerViewAdapter(
                    onItemClicked = { homeDataModel ->
//                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(homeDataModel)
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsComposeFragment2(homeDataModel)
                        findNavController().navigate(action)

                    })

            paginationScrollListener = PaginationScrollListener(
                    linearLayoutManager,
                    {
                        if (searchEditText.text.toString().isNotEmpty()) {
                            binding?.recyclerViewLayout?.moreProgressView?.visibility = View.VISIBLE
                        }
                        viewModel?.fetchSearchResult()
                    },
                    Definitions.PAGINATION_SIZE
            )
            paginationScrollListener?.let {
                binding?.recyclerViewLayout?.homeRecyclerView?.addOnScrollListener(it)
            }
            binding?.recyclerViewLayout?.homeRecyclerView?.adapter = homeRecyclerViewAdapter
        }
    }

    private fun handleClickSearch() {
        binding?.recyclerViewLayout?.homeRecyclerView?.smoothScrollToPosition(Definitions.FIRST_POSITION)
        binding?.searchEditText?.clearFocus()
        activity?.let { closeSoftKeyboard(it) }
        viewModel?.searchForResults()
    }

    private fun fragmentResult() {
        setFragmentResultListener(ACTIVITY_RESULT.DETAILS) { _: String, bundle: Bundle ->
            viewModel?.updateModel(bundle.getParcelable(BUNDLE.MOVIE_DETAILS))
        }
    }
}
