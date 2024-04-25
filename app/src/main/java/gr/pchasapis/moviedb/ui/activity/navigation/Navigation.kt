package gr.pchasapis.moviedb.ui.activity.navigation

sealed class Navigation(val route:String) {

    data object Home : Navigation("home")
    data object Details : Navigation("details")
}