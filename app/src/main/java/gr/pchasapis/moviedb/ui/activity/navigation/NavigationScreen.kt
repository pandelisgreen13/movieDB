package gr.pchasapis.moviedb.ui.activity.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.gson.Gson
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.fragment.details.DetailsRoute
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeRoute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Navigation.Home) {
        composable<Navigation.Home> {
            HomeRoute { movie ->
                navController.navigate(Navigation.Details(movie))
            }
        }
        composable<Navigation.Details>(
            typeMap = mapOf(typeOf<HomeDataModel>() to ParcelableType)
        ) { backStackEntry ->
            val post = backStackEntry.toRoute<Navigation.Details>()
            DetailsRoute(passData = post.model)
        }
    }
}

val ParcelableType = object : NavType<HomeDataModel>(false) {
    override fun get(bundle: Bundle, key: String): HomeDataModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, HomeDataModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): HomeDataModel {
        return Json.decodeFromString<HomeDataModel>(value)
    }

    override fun put(bundle: Bundle, key: String, value: HomeDataModel) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: HomeDataModel): String {
        return Uri.encode(Json.encodeToString(value))
    }
}