package gr.pchasapis.moviedb.ui.activity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import gr.pchasapis.moviedb.ui.fragment.details.DetailsRoute
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeRoute

@Composable
fun AppNavHost(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Navigation.Home.route) {
        composable(Navigation.Home.route) {
            HomeRoute()
        }
        composable(Navigation.Details.route) {
            DetailsRoute()
        }
//        composable(
//            "plantDetail/{plantId}",
//            arguments = listOf(navArgument("plantId") { type = NavType.StringType })
//        ) {
//            PlantDetailsScreen(/* ... */)
//        }
//        composable(
//            "gallery/{plantName}",
//            arguments = listOf(navArgument("plantName") { type = NavType.StringType })
//        ) {
//            GalleryScreen(/* ... */)
//        }
    }
}