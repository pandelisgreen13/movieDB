package gr.pchasapis.moviedb.mvvm.interactor.home.theater

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.mappers.HomeDataModelMapperImpl
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl.Companion.DATE_FROM
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl.Companion.DATE_TO
import gr.pchasapis.moviedb.network.client.MovieClient
import retrofit2.HttpException
import java.io.IOException

class TheaterPagingDataSource(
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
            val response = movieClient.getMovieTheatre()

            val nextPage = if (response.page == response.totalPages) {
                null
            } else {
                currentPage + 1
            }

            val prevKey = if (currentPage <= 1) null else currentPage - 1

            return LoadResult.Page(
                data = mapper.toHomeDataModelFromTheater(response),
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
