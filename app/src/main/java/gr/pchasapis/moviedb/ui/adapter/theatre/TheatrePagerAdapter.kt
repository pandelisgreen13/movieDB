package gr.pchasapis.moviedb.ui.adapter.theatre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import gr.pchasapis.moviedb.databinding.RowTheatreBinding
import gr.pchasapis.moviedb.model.data.MovieDataModel

class TheatrePagerAdapter(private val moviesList: List<MovieDataModel>) : RecyclerView.Adapter<TheatrePagerAdapter.TheatreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheatreViewHolder {
        val itemBinding = RowTheatreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TheatreViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TheatreViewHolder, position: Int) {
        val item = moviesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class TheatreViewHolder(private val containerView: RowTheatreBinding) : RecyclerView.ViewHolder(containerView.root) {
        fun bind(item: MovieDataModel) {
            Picasso.get().load(item.thumbnail).into(containerView.imageView)
        }
    }
}