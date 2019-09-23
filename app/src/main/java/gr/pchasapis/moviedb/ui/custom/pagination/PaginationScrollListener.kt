package gr.pchasapis.moviedb.ui.custom.pagination

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val askForMore: () -> Unit,
        private val offset: Int
) : RecyclerView.OnScrollListener() {
    private var finishedPagination: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!finishedPagination && visibleItemCount + firstVisibleItemPosition >= totalItemCount - offset && firstVisibleItemPosition >= 0 && totalItemCount >= offset) {
            askForMore()
        }
    }

    @Synchronized
    fun finishedPagination(finishedPagination: Boolean) {
        this.finishedPagination = finishedPagination
    }
}