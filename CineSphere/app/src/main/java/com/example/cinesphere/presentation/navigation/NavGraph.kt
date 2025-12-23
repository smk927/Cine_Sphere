
package com.example.cinesphere.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cinesphere.presentation.allitems.AllItemsScreen
import com.example.cinesphere.presentation.chats.ChatsScreen
import com.example.cinesphere.presentation.details.MediaDetailsScreen
import com.example.cinesphere.presentation.home.HomeScreen
import com.example.cinesphere.presentation.search.SearchScreen
import com.example.cinesphere.presentation.wishlist.WishlistScreen

@Composable
fun CineSphereNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = AppDestinations.HOME.route, modifier = modifier) {
        composable(AppDestinations.HOME.route) {
            HomeScreen(
                onMediaClick = { mediaId, mediaType ->
                    navController.navigate("details/$mediaId/$mediaType")
                },
                onViewAllClick = { type, genreId, genreName ->
                    navController.navigate(AllItemsNavigation.allItemsRoute(type, genreId, genreName))
                }
            )
        }
        composable(AppDestinations.WISHLIST.route) {
            WishlistScreen(onMediaClick = { mediaId, mediaType ->
                navController.navigate("details/$mediaId/$mediaType")
            })
        }
        composable(AppDestinations.CHATS.route) {
            ChatsScreen()
        }
        composable(SearchNavigation.route) {
            SearchScreen(onMediaClick = { mediaId, mediaType ->
                navController.navigate("details/$mediaId/$mediaType")
            })
        }
        composable(
            route = "details/{mediaId}/{mediaType}",
            arguments = listOf(
                navArgument("mediaId") { type = NavType.IntType },
                navArgument("mediaType") { type = NavType.StringType }
            )
        ) { 
            MediaDetailsScreen(
                viewModel = hiltViewModel(),
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable(
            route = AllItemsNavigation.route,
            arguments = AllItemsNavigation.arguments
        ) {
            AllItemsScreen(
                onBackPressed = { navController.popBackStack() },
                onMediaClick = { mediaId, mediaType ->
                    navController.navigate("details/$mediaId/$mediaType")
                }
            )
        }
    }
}
