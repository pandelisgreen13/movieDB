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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun AppNavHost(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Navigation.Home.route) {
        composable(
            route = Navigation.Home.route,
        ) {
            HomeRoute() {
                val model: String = Json.encodeToString(HomeDataModel.serializer(), it)
                val json = Uri.encode(Gson().toJson(it))
                navController.navigate("profile/$json")
            }
        }
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = ParcelableType() })
        ) { backStackEntry ->
//            val modelString = backStackEntry.arguments?.getString("userId")
//            val model = modelString?.let { Json.decodeFromString<HomeDataModel>(it) }

            val post = backStackEntry.arguments?.getParcelable<HomeDataModel>("userId")
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