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
) : PagingSource<String, HomeDataModel>() {

    override fun getRefreshKey(state: PagingState<String, HomeDataModel>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey
        }.toString()
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, HomeDataModel> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber: Int = params.key?.toInt() ?: STARTING_PAGE_INDEX
            val response = movieClient.getSearchAsync(queryText, nextPageNumber)
            return LoadResult.Page(
                    data = mapper.toHomeDataModelFromResponse(response),
                    prevKey = null, // Only paging forward.
                    nextKey = nextPageNumber.toString()
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object{

        private const val STARTING_PAGE_INDEX = 1
    }
}
