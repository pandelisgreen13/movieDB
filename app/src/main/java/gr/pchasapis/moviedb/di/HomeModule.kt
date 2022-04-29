package gr.pchasapis.moviedb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.pchasapis.moviedb.database.MovieDbDatabase
import gr.pchasapis.moviedb.mvvm.interactor.home.HomeInteractorImpl
import gr.pchasapis.moviedb.network.client.MovieClient

@InstallIn(SingletonComponent::class)
@Module
object HomeModule {

    @Provides
    fun provideHomeInteractor(movieClient: MovieClient,
                              movieDbDatabase: MovieDbDatabase): HomeInteractorImpl {
        return HomeInteractorImpl(movieClient, movieDbDatabase)
    }
}
