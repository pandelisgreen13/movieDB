package gr.pchasapis.moviedb.ui.activity.theatre

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.extensions.setMargins
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.ui.adapter.theatre.TheatrePagerAdapter
import kotlinx.android.synthetic.main.activity_theatre.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class TheatreActivity : AppCompatActivity() {

    private lateinit var moviesList: List<MovieDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theatre)
        getExtras()
        initLayout()
    }

    private fun getExtras() {
        moviesList = intent?.extras?.getParcelableArrayList<MovieDataModel>(BUNDLE.MOVIE_THEATRE)?.toList() ?: arrayListOf()
    }

    private fun initLayout() {
        toolbarTitleTextView.text = getString(R.string.theatre_in_movies)
        backButtonImageView.visibility = View.VISIBLE
        actionButtonImageView.visibility = View.INVISIBLE
        backButtonImageView.setOnClickListener { onBackPressed() }

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.common_twenty_dp)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.common_thirty_dp)

        viewPager.apply {
            adapter = TheatrePagerAdapter(moviesList)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setMargins(offsetPx, pageMarginPx)
        }
    }
}
