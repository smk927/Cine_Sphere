package com.example.cinesphere.presentation.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cinesphere.presentation.allitems.AllItemsScreen
import com.example.cinesphere.presentation.auth.AuthScreen
import com.example.cinesphere.presentation.chats.ChatsScreen
import com.example.cinesphere.presentation.chats.GenreChatScreen
import com.example.cinesphere.presentation.chats.GenreSelectionScreen
import com.example.cinesphere.presentation.details.MediaDetailsScreen
import com.example.cinesphere.presentation.discover.DiscoverScreen
import com.example.cinesphere.presentation.games.GamesScreen
import com.example.cinesphere.presentation.home.HomeScreen
import com.example.cinesphere.presentation.map.CinemaMapScreen
import com.example.cinesphere.presentation.profile.ProfileScreen
import com.example.cinesphere.presentation.wishlist.WishlistScreen
import com.example.cinesphere.presentation.subscription.SubscriptionScreen
import com.example.cinesphere.presentation.subscription.SubscriptionPromptScreen

@Composable
fun CineSphereNavHost(
    navController: NavHostController, 
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    onThemeToggle: (Boolean) -> Unit = {},
    startDestination: String = AppDestinations.HOME.route,
    homeListState: LazyListState // Added parameter for scroll-up
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(AppDestinations.HOME.route) {
            HomeScreen(
                onMediaClick = { mediaId, mediaType ->
                    navController.navigate("details/$mediaId/$mediaType")
                },
                onViewAllClick = { type, genreId, genreName ->
                    navController.navigate(AllItemsNavigation.allItemsRoute(type, genreId, genreName))
                },
                onSubscriptionClick = {
                    navController.navigate(SubscriptionPromptNavigation.createRoute(GamesNavigation.route))
                },
                onGamesClick = {
                    navController.navigate(GamesNavigation.route)
                },
                listState = homeListState // Pass the state
            )
        }
        composable(AppDestinations.WISHLIST.route) {
            WishlistScreen(onMediaClick = { mediaId, mediaType ->
                navController.navigate("details/$mediaId/$mediaType")
            })
        }
        composable(AppDestinations.DISCOVER.route) {
            DiscoverScreen(onMediaClick = { mediaId, mediaType ->
                navController.navigate("details/$mediaId/$mediaType")
            })
        }
        composable(AppDestinations.CHATS.route) {
            GenreSelectionScreen(
                onGenreClick = { genreId ->
                    navController.navigate(GenreChatNavigation.createChatRoute(genreId))
                }
            )
        }
        composable(
            route = GenreChatNavigation.chatRoute,
            arguments = listOf(navArgument("genreId") { type = NavType.StringType })
        ) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getString("genreId")
            if (genreId != null) {
                GenreChatScreen(
                    genreId = genreId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
        composable(ProfileNavigation.route) {
            ProfileScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onSignOut = {
                    navController.navigate(AuthNavigation.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(CinemaMapNavigation.route) {
            CinemaMapScreen()
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
        composable(
            route = SubscriptionNavigation.route,
            arguments = listOf(navArgument(SubscriptionNavigation.redirectArg) { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val redirectRoute = backStackEntry.arguments?.getString(SubscriptionNavigation.redirectArg)
            SubscriptionScreen(
                onSubscriptionSuccess = {
                    if (redirectRoute != null) {
                        navController.navigate(redirectRoute) {
                            popUpTo(AppDestinations.HOME.route)
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = SubscriptionPromptNavigation.route,
            arguments = listOf(navArgument(SubscriptionPromptNavigation.redirectArg) { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val redirectRoute = backStackEntry.arguments?.getString(SubscriptionPromptNavigation.redirectArg)
            SubscriptionPromptScreen(
                onProceedToPayment = {
                    navController.navigate(SubscriptionNavigation.createRoute(redirectRoute))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(GamesNavigation.route) {
            GamesScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(AuthNavigation.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(AppDestinations.HOME.route) {
                        popUpTo(AuthNavigation.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
