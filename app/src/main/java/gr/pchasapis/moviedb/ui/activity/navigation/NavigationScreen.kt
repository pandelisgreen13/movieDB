package gr.pchasapis.moviedb.ui.activity.navigation

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.os.BundleCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.fragment.details.DetailsRoute
import gr.pchasapis.moviedb.ui.fragment.favourite.screen.FavouriteRoute
import gr.pchasapis.moviedb.ui.fragment.home.HomeViewModel
import gr.pchasapis.moviedb.ui.fragment.home.compose.HomeRoute
import gr.pchasapis.moviedb.ui.fragment.theater.TheaterViewModel
import gr.pchasapis.moviedb.ui.fragment.theater.TheatreScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Navigation.Home) {
        composable<Navigation.Home>() {

            val homeViewModel: HomeViewModel = hiltViewModel()
            val movies by homeViewModel.uiState.collectAsStateWithLifecycle()

            HomeRoute(
                movies = movies,
                onItemClicked = { movie ->
                    navController.navigate(Navigation.Details(movie))
                },
                textChanged = {
                    homeViewModel.setQueryText(it)
                })
        }
        composable<Navigation.Details>(
            typeMap = mapOf(typeOf<HomeDataModel>() to parcelableType<HomeDataModel>())
        ) { backStackEntry ->
            val post = backStackEntry.toRoute<Navigation.Details>()
            DetailsRoute(passData = post.model) {
                navController.navigateUp()
            }
        }

        composable<Navigation.Favourites> {
            FavouriteRoute { movie ->
                navController.navigate(Navigation.Details(movie))
            }
        }
        composable<Navigation.Theater> {

            val viewModel: TheaterViewModel = hiltViewModel()

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            TheatreScreen(uiState)
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