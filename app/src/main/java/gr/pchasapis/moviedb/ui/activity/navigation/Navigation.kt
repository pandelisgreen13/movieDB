package gr.pchasapis.moviedb.ui.activity.navigation

import gr.pchasapis.moviedb.model.data.HomeDataModel
import kotlinx.serialization.Serializable

sealed class Navigation {

    @Serializable
    object Home

    @Serializable
    data class Details(
        val model: HomeDataModel
    )

    @Serializable
    object Favourites
}
