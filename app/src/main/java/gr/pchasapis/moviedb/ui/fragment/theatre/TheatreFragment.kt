package gr.pchasapis.moviedb.ui.fragment.theatre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.extensions.setMargins
import gr.pchasapis.moviedb.databinding.ActivityTheatreBinding
import gr.pchasapis.moviedb.model.data.MovieDataModel
import gr.pchasapis.moviedb.ui.adapter.theatre.TheatrePagerAdapter

class TheatreFragment : Fragment() {

    private lateinit var moviesList: List<MovieDataModel>
    private lateinit var binding: ActivityTheatreBinding
    private val args: TheatreFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityTheatreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getExtras()
        initLayout()
    }

    private fun getExtras() {
        moviesList = args.movieDataModel.list
    }

    private fun initLayout() = with(binding) {
        toolbarLayout.toolbarTitleTextView.text = getString(R.string.theatre_in_movies)
        toolbarLayout.backButtonImageView.visibility = View.VISIBLE
        toolbarLayout.actionButtonImageView.visibility = View.INVISIBLE
        toolbarLayout.backButtonImageView.setOnClickListener {
            val navHost= parentFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val backStackEntryCount = navHost?.parentFragmentManager?.backStackEntryCount?:0

            findNavController().navigateUp()
        }

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
