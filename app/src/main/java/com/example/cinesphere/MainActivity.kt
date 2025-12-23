package com.example.cinesphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cinesphere.presentation.navigation.AppDestinations
import com.example.cinesphere.presentation.navigation.AuthNavigation
import com.example.cinesphere.presentation.navigation.CinemaMapNavigation
import com.example.cinesphere.presentation.navigation.CineSphereNavHost
import com.example.cinesphere.presentation.navigation.ProfileNavigation
import com.example.cinesphere.ui.theme.CineSphereTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.collectAsState
import com.example.cinesphere.data.session.UserSession
import com.example.cinesphere.presentation.navigation.SubscriptionNavigation
import com.example.cinesphere.presentation.navigation.SubscriptionPromptNavigation
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val startDestination = if (firebaseAuth.currentUser != null) {
            AppDestinations.HOME.route
        } else {
            AuthNavigation.route
        }

        setContent {
            // Defaulting to dark theme
            var isDarkTheme by remember { mutableStateOf(true) }
            
            CineSphereTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = it },
                    startDestination = startDestination
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    startDestination: String
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSubscribed by UserSession.isSubscribed.collectAsState()

    val shouldShowTopBar = currentRoute in AppDestinations.entries.map { it.route } || currentRoute == ProfileNavigation.route || currentRoute == CinemaMapNavigation.route
    
    val homeListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (shouldShowTopBar) {
                CenterAlignedTopAppBar(
                    title = { Text("Cine Sphere") },
                    navigationIcon = {
                        if (currentRoute == ProfileNavigation.route || currentRoute == CinemaMapNavigation.route) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    actions = {
                        if (currentRoute in AppDestinations.entries.map { it.route }) {
                            IconButton(onClick = { navController.navigate(CinemaMapNavigation.route) }) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Cinema Locator")
                            }
                            IconButton(onClick = { navController.navigate(ProfileNavigation.route) }) {
                                Icon(Icons.Default.Person, contentDescription = "User Profile")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute != AuthNavigation.route && currentRoute != ProfileNavigation.route && currentRoute != CinemaMapNavigation.route) {
                NavigationBar {
                    AppDestinations.entries.forEach { destination ->
                        NavigationBarItem(
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) },
                            selected = destination.route == currentRoute,
                            onClick = {
                                if (destination.route == currentRoute) {
                                    if(destination.route == AppDestinations.HOME.route) {
                                         coroutineScope.launch {
                                            homeListState.animateScrollToItem(0)
                                        }
                                    }
                                } else {
                                    if (destination == AppDestinations.CHATS && !isSubscribed) {
                                        navController.navigate(SubscriptionPromptNavigation.createRoute(AppDestinations.CHATS.route))
                                    } else {
                                        navController.navigate(destination.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        CineSphereNavHost(
            navController = navController, 
            modifier = Modifier.padding(innerPadding),
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle,
            startDestination = startDestination,
            homeListState = homeListState
        )
    }
}