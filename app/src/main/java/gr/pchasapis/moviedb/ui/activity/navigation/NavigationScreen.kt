package gr.pchasapis.moviedb.ui.activity.navigation

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.fragment.details.DetailsRoute
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeRoute

@Composable
fun AppNavHost(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Navigation.Home.route) {
        composable(
            route = Navigation.Home.route,
        ) {
            HomeRoute {
                val model = Uri.encode(Gson().toJson(it))
                navController.navigate("${Navigation.Details.route}/$model")
            }
        }
        composable(
            route = "${Navigation.Details.route}/{model}",
            arguments = listOf(navArgument("model") { type = ParcelableType() })
        ) { backStackEntry ->
            val post = backStackEntry.arguments?.getParcelable<HomeDataModel>("model")
            DetailsRoute(passData = post)
        }
    }
}

class ParcelableType : NavType<HomeDataModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): HomeDataModel? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): HomeDataModel {
        return Gson().fromJson(value, HomeDataModel::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: HomeDataModel) {
        bundle.putParcelable(key, value)
    }
}