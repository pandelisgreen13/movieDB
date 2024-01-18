package gr.pchasapis.moviedb.ui.activity.navigation

sealed class Navigation(val route:String) {

    data object Home : Navigation("Home")
    data object Details : Navigation("Details")
}