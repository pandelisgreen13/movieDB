package gr.pchasapis.moviedb.mvvm.interactor.home.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import gr.pchasapis.moviedb.network.client.MovieClient
import retrofit2.HttpException
import java.io.IOException

class SearchPagingDataSource(
    private val queryText: String,
    private var movieClient: MovieClient,
    private val mapper: HomeDataModelMapperImpl
) : PagingSource<Int, HomeDataModel>() {

    override fun getRefreshKey(state: PagingState<Int, HomeDataModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeDataModel> {
        return try {
            // Start refresh at page 1 if undefined.
            val currentPage: Int = params.key ?: STARTING_PAGE_INDEX
            val response = movieClient.getSearchAsync(queryText, currentPage)

            val nextPage = if (response.page == response.totalPages) {
                null
            } else {
                currentPage + 1
            }

            val prevKey = if (currentPage <= 1) null else currentPage - 1

            return LoadResult.Page(
                data = mapper.toHomeDataModelFromResponse(response),
                prevKey = prevKey, // Only paging forward.
                nextKey = nextPage
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {

        private const val STARTING_PAGE_INDEX = 1
    }
}
