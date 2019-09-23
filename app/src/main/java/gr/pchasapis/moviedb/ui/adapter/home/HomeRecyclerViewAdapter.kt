package gr.pchasapis.moviedb.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.common.extensions.loadUrl
import gr.pchasapis.moviedb.model.data.HomeDataModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_home.*

class HomeRecyclerViewAdapter(private val onItemClicked: (HomeDataModel) -> Unit) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ItemViewHolder>() {

    private var homeDataModelList: MutableList<HomeDataModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_home, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val homeDataModel = homeDataModelList[position]
        holder.titleTextView.text = homeDataModel.title
        holder.thumbnailImageView.loadUrl(homeDataModel.thumbnail)
        holder.releaseDateTextView.text = homeDataModel.releaseDate
        holder.ratingTextView.text = homeDataModel.ratings
        holder.itemView.setOnClickListener { onItemClicked(homeDataModel) }
    }

    override fun getItemCount(): Int {
        return homeDataModelList.size
    }

    class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

    fun setSearchList(searchList: MutableList<HomeDataModel>) {
        homeDataModelList.clear()
        homeDataModelList.addAll(searchList)
        notifyDataSetChanged()
    }
}