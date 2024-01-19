package gr.pchasapis.moviedb.ui.activity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.fragment.details.DetailsRoute
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeRoute
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun AppNavHost(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Navigation.Home.route) {
        composable(
            route = Navigation.Home.route,
        ) {
            HomeRoute() {
                val model = Json.encodeToString(HomeDataModel.serializer(), it)
                navController.navigate("${Navigation.Details.route}/$model")
            }
        }
        composable(
            route = "${Navigation.Details.route}/{model}",
            arguments = listOf(navArgument("model") { type = NavType.StringType }),
        ) { backStackEntry ->
            val modelString = backStackEntry.arguments?.getString("model")
            val model = modelString?.let { Json.decodeFromString<HomeDataModel>(it) }
            DetailsRoute(passData = model)
        }
    }
}