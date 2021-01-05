package gr.pchasapis.moviedb.ui.activity.theatre

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.BUNDLE
import gr.pchasapis.moviedb.common.extensions.setMargins
import gr.pchasapis.moviedb.databinding.ActivityTheatreBinding
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.ui.adapter.theatre.TheatrePagerAdapter

class TheatreActivity : AppCompatActivity() {

    private lateinit var moviesList: List<MovieDataModel>
    private lateinit var binding: ActivityTheatreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTheatreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getExtras()
        initLayout()
    }

    private fun getExtras() {
        moviesList = intent?.extras?.getParcelableArrayList<MovieDataModel>(BUNDLE.MOVIE_THEATRE)?.toList() ?: arrayListOf()
    }

    private fun initLayout() {
        binding.toolbarLayout.toolbarTitleTextView.text = getString(R.string.theatre_in_movies)
        binding.toolbarLayout.backButtonImageView.visibility = View.VISIBLE
        binding.toolbarLayout. actionButtonImageView.visibility = View.INVISIBLE
        binding.toolbarLayout. backButtonImageView.setOnClickListener { onBackPressed() }

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.common_twenty_dp)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.common_thirty_dp)

       binding.viewPager.apply {
            adapter = TheatrePagerAdapter(moviesList)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setMargins(offsetPx, pageMarginPx)
        }
    }
}
