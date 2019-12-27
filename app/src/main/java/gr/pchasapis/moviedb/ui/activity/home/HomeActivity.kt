package gr.pchasapis.moviedb.ui.activity.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import closeSoftKeyboard
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.common.ACTIVITY_RESULT
import gr.pchasapis.moviedb.common.application.MovieApplication
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.mvvm.viewModel.base.BaseViewModelFactory
import gr.pchasapis.moviedb.mvvm.viewModel.home.HomeViewModel
import gr.pchasapis.moviedb.ui.activity.base.BaseActivity
import gr.pchasapis.moviedb.ui.activity.details.DetailsActivity
import gr.pchasapis.moviedb.ui.adapter.home.HomeRecyclerViewAdapter
import gr.pchasapis.moviedb.ui.custom.pagination.PaginationScrollListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_pagination_recyclerview.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class HomeActivity : BaseActivity<HomeViewModel>() {

    private var homeRecyclerViewAdapter: HomeRecyclerViewAdapter? = null
    private var paginationScrollListener: PaginationScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initLayout()
        initViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_RESULT.DETAILS && resultCode == Activity.RESULT_OK) {
            viewModel?.updateModel(data?.getParcelableExtra(BUNDLE.MOVIE_DETAILS))
            viewModel?.readWatchListFromDatabase()
        }
    }

    override fun onBackPressed() {
        if (viewModel?.isWatchListMode == true) {
            viewModel?.showWatchList()
            return
        }
        super.onBackPressed()
    }

    private fun initViewModel() {
        val dashboardViewModelFactory = BaseViewModelFactory { HomeViewModel(HomeInteractorImpl(MovieApplication.get()?.movieClient!!, MovieDbDatabase.get(this))) }

        viewModel = ViewModelProviders.of(this, dashboardViewModelFactory).get(HomeViewModel::class.java)
        initViewModelState()
        viewModel?.getSearchList()?.observe(this, Observer { resultList ->
            resultList?.let {
                homeRecyclerViewAdapter?.setSearchList(it)
            } ?: run { emptyView.visibility = View.VISIBLE }
        })

        viewModel?.getToolbarTitle()?.observe(this, Observer { value ->
            value?.let { isWatchlistMode ->
                toolbarTitleTextView.text = when {
                    isWatchlistMode -> getString(R.string.home_watch_list)
                    else -> getString(R.string.home_toolbar_title)
                }
            }
        })

        viewModel?.getPaginationStatus()?.observe(this, Observer { value ->
            value?.let { isPaginationFinished ->
                paginationScrollListener?.finishedPagination(isPaginationFinished)
            }
        })

        viewModel?.getPaginationLoader()?.observe(this, Observer { value ->
            value?.let { show ->
                moreProgressView?.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel?.getWatchListLiveData()?.observe(this, Observer { value ->
            value?.let { hasWatchListItems ->
                if (hasWatchListItems) {
                    watchListButton.show()
                } else {
                    watchListButton.hide()
                    toolbarTitleTextView.text = getString(R.string.home_toolbar_title)
                }
            }
        })
    }

    private fun initLayout() {
        toolbarTitleTextView.text = getString(R.string.home_toolbar_title)
        backButtonImageView.visibility = View.INVISIBLE
        actionButtonImageView.visibility = View.VISIBLE
        actionButtonImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_theatre))

        actionButtonImageView.setOnClickListener {

        }

        searchImageButton.setOnClickListener {
            if (searchEditText.text.toString().isEmpty() && viewModel?.isWatchListMode == false) {
                showErrorDialog(getString(R.string.home_empty_field), closeListener = { dialog ->
                    dialog.dismiss()
                    viewModel?.genericErrorLiveData?.value = false
                })
            }
            handleClickSearch()
        }
        watchListButton.setOnClickListener {
            searchEditText.setText("")
            viewModel?.showWatchList()
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

        val linearLayoutManager = LinearLayoutManager(this)
        homeRecyclerView.layoutManager = linearLayoutManager
        homeRecyclerViewAdapter = HomeRecyclerViewAdapter(
                onItemClicked = { homeDataModel ->
                    val intent = Intent(this, DetailsActivity::class.java)
                    intent.putExtra(BUNDLE.MOVIE_DETAILS, homeDataModel)
                    startActivityForResult(intent, ACTIVITY_RESULT.DETAILS)
                })

        paginationScrollListener = PaginationScrollListener(
                linearLayoutManager,
                {
                    if (viewModel?.isWatchListMode == true) {
                        return@PaginationScrollListener
                    }
                    if (searchEditText.text.toString().isNotEmpty()) {
                        moreProgressView?.visibility = View.VISIBLE
                    }
                    viewModel?.fetchSearchResult()
                },
                Definitions.PAGINATION_SIZE
        )
        paginationScrollListener?.let {
            homeRecyclerView.addOnScrollListener(it)
        }
        homeRecyclerView.adapter = homeRecyclerViewAdapter
    }

    private fun handleClickSearch() {
        homeRecyclerView.smoothScrollToPosition(Definitions.FIRST_POSITION)
        searchEditText.clearFocus()
        closeSoftKeyboard(this)
        viewModel?.searchForResults()
    }
}
