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
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, HomeDataModel> {
        return try {
            // Start refresh at page 1 if undefined.
            var nextPageNumber: Int = params.key?.toIntOrNull() ?: STARTING_PAGE_INDEX
            val response = movieClient.getSearchAsync(queryText, nextPageNumber ?: 0)

            nextPageNumber += 1

            if (nextPageNumber == 3) {
                nextPageNumber = 0
            }

            return LoadResult.Page(
                data = mapper.toHomeDataModelFromResponse(response),
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber.toString()
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
