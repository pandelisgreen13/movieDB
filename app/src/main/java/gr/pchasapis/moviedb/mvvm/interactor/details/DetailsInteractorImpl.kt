package gr.pchasapis.moviedb.mvvm.interactor.details

import gr.pchasapis.moviedb.common.Definitions
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.database.dao.MovieDbTable
import gr.pchasapis.moviedb.model.common.DataResult
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.model.data.SimilarMoviesModel
import gr.pchasapis.moviedb.model.parsers.genre.GenresItem
import gr.pchasapis.moviedb.model.parsers.movie.MovieResponse
import gr.pchasapis.moviedb.model.parsers.tv.TvShowResponse
import gr.pchasapis.moviedb.mvvm.interactor.base.BaseInteractor
import gr.pchasapis.moviedb.network.client.MovieClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DetailsInteractorImpl @Inject constructor(
    private var movieClient: MovieClient,
    private val movieDbDatabase: MovieDbDatabase,
) : BaseInteractor(), DetailsInteractor {

    override suspend fun getSimilarMovies(
        id: Int,
        mediaType: String
    ): DataResult<List<SimilarMoviesModel>> {
        return try {
            val response = movieClient.getSimilarMovies(id = id, mediaType = mediaType)
            DataResult(response.searchResultsList?.map {
                SimilarMoviesModel(
                    id = it.id ?: -1,
                    image = "${Definitions.IMAGE_URL_W500}${it.posterPath}"
                )
            })
        } catch (t: Throwable) {
            DataResult(throwable = t)
        }
    }

    override suspend fun onRetrieveFlowDetails(homeDataModel: HomeDataModel): Flow<DataResult<HomeDataModel>> {
        return flow {
            emit(onRetrieveDetails(homeDataModel))
        }.distinctUntilChanged()
    }

    override suspend fun onRetrieveDetails(homeDataModel: HomeDataModel): DataResult<HomeDataModel> {
        return try {
            val response = when (homeDataModel.mediaType) {
                Definitions.IS_MOVIE -> movieToHomeDataModel(
                    homeDataModel = homeDataModel,
                    movieResponse = movieClient.getMovieDetailsAsync(homeDataModel.id ?: 0)
                )

                else -> tvShowToHomeDataModel(
                    homeDataModel, movieClient.getTvShowDetailsAsync(tvId = homeDataModel.id ?: 0)
                )
            }
            DataResult(response)
        } catch (t: Throwable) {
            Timber.d(t)
            DataResult(throwable = t)
        }
    }

    override suspend fun updateFavourite(homeDataModel: HomeDataModel?): DataResult<HomeDataModel> {
        return try {
            homeDataModel?.let {
                if (homeDataModel.isFavorite) {
                    movieDbDatabase.movieDbTableDao().insertModel(toDatabaseModel(homeDataModel))
                } else {
                    it.id?.let { id -> movieDbDatabase.movieDbTableDao().deleteModel(id) }
                }
            }
            DataResult(homeDataModel)
        } catch (t: Throwable) {
            Timber.d(t)
            DataResult(throwable = t)
        }
    }

    private fun toDatabaseModel(homeDataModel: HomeDataModel): MovieDbTable {
        return MovieDbTable(
            id = homeDataModel.id ?: 0,
            title = homeDataModel.title ?: "-",
            mediaType = homeDataModel.mediaType ?: "-",
            summary = homeDataModel.summary ?: "-",
            thumbnail = homeDataModel.thumbnail ?: "",
            releaseDate = homeDataModel.releaseDate ?: "-",
            ratings = homeDataModel.ratings ?: "-",
            isFavourite = homeDataModel.isFavorite,
            genresName = homeDataModel.genresName ?: "-",
            videoKey = homeDataModel.videoKey ?: "",
            videoUrl = homeDataModel.videoUrl ?: "",
            dateAdded = System.currentTimeMillis()
        )
    }

    private fun movieToHomeDataModel(
        homeDataModel: HomeDataModel,
        movieResponse: MovieResponse
    ): HomeDataModel {
        homeDataModel.thumbnail = "${Definitions.IMAGE_URL_W500}${movieResponse.posterPath}"
        homeDataModel.genresName = getGenre(movieResponse.genres)
        homeDataModel.videoKey = movieResponse.videos?.videoResultList?.firstOrNull()?.key ?: ""
        homeDataModel.videoUrl =
            "<html><body><br><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${homeDataModel.videoKey}\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
        return homeDataModel
    }

    private fun getGenre(genres: List<GenresItem>?) =
        genres?.map {
            it.name.orEmpty()
        }?.filter { it.isEmpty().not() }?.joinToString("\n") { "${it}" }

    private fun tvShowToHomeDataModel(
        homeDataModel: HomeDataModel,
        tvShowResponse: TvShowResponse
    ): HomeDataModel {
        homeDataModel.thumbnail = "${Definitions.IMAGE_URL_W500}${tvShowResponse.posterPath}"
        homeDataModel.genresName = getGenre(tvShowResponse.genres)
        homeDataModel.videoKey = tvShowResponse.videos?.videoResultList?.firstOrNull()?.key ?: ""
        homeDataModel.videoUrl =
            "<html><body><br><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${homeDataModel.videoKey}\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
        return homeDataModel
    }

}
