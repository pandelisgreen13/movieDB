package gr.pchasapis.moviedb.ui.adapter.theatre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.MovieDataModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_theatre.*

class TheatrePagerAdapter(private val moviesList: List<MovieDataModel>) : RecyclerView.Adapter<TheatrePagerAdapter.TheatreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheatreViewHolder {
        return TheatreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_theatre, parent, false))
    }

    override fun onBindViewHolder(holder: TheatreViewHolder, position: Int) {
        val item = moviesList[position]
        Picasso.get().load(item.thumbnail).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class TheatreViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}