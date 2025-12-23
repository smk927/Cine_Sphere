package com.example.cinesphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cinesphere.presentation.navigation.AppDestinations
import com.example.cinesphere.presentation.navigation.CineSphereNavHost
import com.example.cinesphere.ui.theme.CineSphereTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineSphereTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowTopBar = currentRoute in AppDestinations.entries.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (shouldShowTopBar) {
                CenterAlignedTopAppBar(
                    title = { Text("CineSphere") },
                    actions = {
                        if (currentRoute == AppDestinations.HOME.route || currentRoute == AppDestinations.WISHLIST.route) {
                            IconButton(onClick = { /* TODO: Handle profile click */ }) {
                                Icon(androidx.compose.material.icons.Icons.Filled.Person, contentDescription = "User Profile")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                AppDestinations.entries.forEach { destination ->
                    NavigationBarItem(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = destination.route == currentRoute,
                        onClick = { 
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        CineSphereNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
