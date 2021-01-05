package gr.pchasapis.moviedb.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.pchasapis.moviedb.common.extensions.loadUrl
import gr.pchasapis.moviedb.databinding.RowHomeBinding
import gr.pchasapis.moviedb.model.data.HomeDataModel

class HomeRecyclerViewAdapter(private val onItemClicked: (HomeDataModel) -> Unit) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ItemViewHolder>() {

    private var homeDataModelList: MutableList<HomeDataModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding = RowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val homeDataModel = homeDataModelList[position]
        holder.bind(homeDataModel)
    }

    override fun getItemCount(): Int {
        return homeDataModelList.size
    }

    class ItemViewHolder(private val rowHomeBinding: RowHomeBinding, private val onItemClicked: (HomeDataModel) -> Unit) : RecyclerView.ViewHolder(rowHomeBinding.root) {
        fun bind(homeDataModel: HomeDataModel) {
            rowHomeBinding.titleTextView.text = homeDataModel.title
            rowHomeBinding.thumbnailImageView.loadUrl(homeDataModel.thumbnail)
            rowHomeBinding.releaseDateTextView.text = homeDataModel.releaseDate
            rowHomeBinding.ratingTextView.text = homeDataModel.ratings
            rowHomeBinding.root.setOnClickListener { onItemClicked(homeDataModel) }
        }
    }

    fun setSearchList(searchList: MutableList<HomeDataModel>) {
        homeDataModelList.clear()
        homeDataModelList.addAll(searchList)
        notifyDataSetChanged()
    }
}