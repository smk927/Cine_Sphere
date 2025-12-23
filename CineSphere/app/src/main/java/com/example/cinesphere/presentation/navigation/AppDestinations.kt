package com.example.cinesphere.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
    SEARCH("search", "Search", Icons.Filled.Search),
    WISHLIST("wishlist", "Wishlist", Icons.Filled.Favorite),
    CHATS("chats", "Chats", Icons.Filled.Info) // Placeholder icon
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
