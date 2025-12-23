package com.example.cinesphere.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.cinesphere.presentation.allitems.ALL_ITEMS_GENRE_ID
import com.example.cinesphere.presentation.allitems.ALL_ITEMS_GENRE_NAME
import com.example.cinesphere.presentation.allitems.ALL_ITEMS_TYPE

enum class AppDestinations(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("home", "Home", Icons.Filled.Home),
    DISCOVER("discover", "Discover", Icons.Filled.Search),
    WISHLIST("wishlist", "Wishlist", Icons.Filled.Favorite),
    CHATS("chats", "Chats", Icons.Filled.Email)
}

object AllItemsNavigation {
    const val route = "allItems/{$ALL_ITEMS_TYPE}?$ALL_ITEMS_GENRE_ID={$ALL_ITEMS_GENRE_ID}&$ALL_ITEMS_GENRE_NAME={$ALL_ITEMS_GENRE_NAME}"
    val arguments = listOf(
        navArgument(ALL_ITEMS_TYPE) { type = NavType.StringType },
        navArgument(ALL_ITEMS_GENRE_ID) { type = NavType.IntType; defaultValue = 0 },
        navArgument(ALL_ITEMS_GENRE_NAME) { type = NavType.StringType; nullable = true }
    )

    fun allItemsRoute(type: String, genreId: Int? = null, genreName: String? = null): String {
        return "allItems/$type".let {
            if (genreId != null && genreName != null) {
                "$it?$ALL_ITEMS_GENRE_ID=$genreId&$ALL_ITEMS_GENRE_NAME=$genreName"
            } else {
                it
            }
        }
    }
}

object SearchNavigation {
    const val route = "search"
}

object ProfileNavigation {
    const val route = "profile"
}

object SubscriptionNavigation {
    const val routeBase = "subscription"
    const val redirectArg = "redirect_route"
    const val route = "$routeBase?$redirectArg={$redirectArg}"
    
    fun createRoute(redirect: String? = null): String {
        return if (redirect != null) {
            "$routeBase?$redirectArg=$redirect"
        } else {
            routeBase
        }
    }
}

object SubscriptionPromptNavigation {
    const val routeBase = "subscription_prompt"
    const val redirectArg = "redirect_route"
    const val route = "$routeBase?$redirectArg={$redirectArg}"

    fun createRoute(redirect: String? = null): String {
        return if (redirect != null) {
            "$routeBase?$redirectArg=$redirect"
        } else {
            routeBase
        }
    }
}

object GamesNavigation {
    const val route = "games"
}

object AuthNavigation {
    const val route = "auth"
}

object CinemaMapNavigation {
    const val route = "cinema_map"
}

object GenreChatNavigation {
    const val selectionRoute = "genre_selection"
    const val chatRouteBase = "genre_chat"
    const val chatRoute = "$chatRouteBase/{genreId}"
    
    fun createChatRoute(genreId: String) = "$chatRouteBase/$genreId"
}