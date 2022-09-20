package gr.pchasapis.moviedb.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gr.pchasapis.moviedb.common.extensions.loadUrl
import gr.pchasapis.moviedb.databinding.RowHomeBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel

class HomeRecyclerViewAdapter(private val onItemClicked: (HomeDataModel?) -> Unit) : PagingDataAdapter<HomeDataModel, HomeRecyclerViewAdapter.ItemViewHolder>(HomeAdapterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding = RowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val homeDataModel = getItem(position)
        holder.bind(homeDataModel)
    }

    class ItemViewHolder(private val rowHomeBinding: RowHomeBinding,
                         private val onItemClicked: (HomeDataModel?) -> Unit
    ) : RecyclerView.ViewHolder(rowHomeBinding.root) {

        fun bind(homeDataModel: HomeDataModel?) {
            rowHomeBinding.titleTextView.text = homeDataModel?.title
            rowHomeBinding.thumbnailImageView.loadUrl(homeDataModel?.thumbnail)
            rowHomeBinding.releaseDateTextView.text = homeDataModel?.releaseDate
            rowHomeBinding.ratingTextView.text = homeDataModel?.ratings
            rowHomeBinding.root.setOnClickListener {
                onItemClicked(homeDataModel)
            }

        }
    }

}

object HomeAdapterComparator : DiffUtil.ItemCallback<HomeDataModel>() {
    override fun areItemsTheSame(oldItem: HomeDataModel, newItem: HomeDataModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HomeDataModel, newItem: HomeDataModel): Boolean {
        return oldItem == newItem
    }

}
