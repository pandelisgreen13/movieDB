package gr.pchasapis.moviedb.ui.activity.navigation

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.core.os.BundleCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
            typeMap = mapOf(typeOf<HomeDataModel>() to parcelableType<HomeDataModel>())
        ) { backStackEntry ->
            val post = backStackEntry.toRoute<Navigation.Details>()
            DetailsRoute(passData = post.model)
        }
    }
}


inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false
) = object : NavType<T>(isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return BundleCompat.getParcelable(bundle, key, T::class.java)
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString<T>(value)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(value))
    }
}