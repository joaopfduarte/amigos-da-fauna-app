package com.app.amigos_da_fauna.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.app.amigos_da_fauna.R
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.ui.AppViewModel
import com.app.amigos_da_fauna.ui.screen.about.AboutScreen
import com.app.amigos_da_fauna.ui.screen.detail.AnimalDetailScreen
import com.app.amigos_da_fauna.ui.screen.home.HomeScreen
import com.app.amigos_da_fauna.ui.screen.map.MapScreen
import com.app.amigos_da_fauna.ui.screen.profile.ProfileScreen
import com.app.amigos_da_fauna.ui.screen.quiz.QuizScreen
import com.app.amigos_da_fauna.ui.screen.register.RegisterScreen
import com.app.amigos_da_fauna.ui.theme.AmigosDaFaunaTheme
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    appViewModel: AppViewModel = hiltViewModel(),
) {
    val themePreference by appViewModel.themePreference.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    AmigosDaFaunaTheme(themePreference = themePreference) {
        val colors = FaunaTheme.colors
        val bottomItems = listOf(
            BottomNavItem(Routes.HOME, stringResource(R.string.nav_home)) {
                Icon(Icons.Outlined.Home, contentDescription = null)
            },
            BottomNavItem(Routes.MAP, stringResource(R.string.nav_map)) {
                Icon(Icons.Outlined.Map, contentDescription = null)
            },
            BottomNavItem(Routes.PROFILE, stringResource(R.string.nav_profile)) {
                Icon(Icons.Outlined.Person, contentDescription = null)
            },
            BottomNavItem(Routes.ABOUT, stringResource(R.string.nav_about)) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            },
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (currentRoute != Routes.REGISTER && currentRoute?.startsWith("animals/") != true &&
                    currentRoute?.startsWith("quiz/") != true
                ) {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_header)) },
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(containerColor = colors.card) {
                        bottomItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = item.icon,
                                label = { Text(item.label) },
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(Routes.HOME) {
                    HomeScreen(
                        onAnimalClick = { id -> navController.navigate(Routes.animalDetail(id)) },
                        onQuizClick = { id -> navController.navigate(Routes.quiz(id)) },
                    )
                }
                composable(Routes.MAP) { MapScreen() }
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        onRegisterClick = { navController.navigate(Routes.REGISTER) },
                        onLoginSuccess = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.findStartDestination().id)
                            }
                        },
                        onThemeChange = appViewModel::setThemePreference,
                    )
                }
                composable(Routes.ABOUT) {
                    AboutScreen(onThemeChange = appViewModel::setThemePreference)
                }
                composable(
                    route = Routes.ANIMAL_DETAIL,
                    arguments = listOf(navArgument("animalId") { type = NavType.IntType }),
                    deepLinks = listOf(
                        navDeepLink { uriPattern = "amigosdafauna://animals/{animalId}" },
                    ),
                ) {
                    AnimalDetailScreen(
                        onQuizClick = { id -> navController.navigate(Routes.quiz(id)) },
                        onBack = { navController.popBackStack() },
                    )
                }
                composable(
                    route = Routes.QUIZ,
                    arguments = listOf(navArgument("animalId") { type = NavType.IntType }),
                ) {
                    QuizScreen(
                        onNavigateHome = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.findStartDestination().id)
                            }
                        },
                        onNavigateProfile = {
                            navController.navigate(Routes.PROFILE) {
                                popUpTo(navController.graph.findStartDestination().id)
                            }
                        },
                    )
                }
                composable(Routes.REGISTER) {
                    RegisterScreen(
                        onLoginClick = { navController.popBackStack() },
                        onSuccess = {
                            navController.navigate(Routes.PROFILE) {
                                popUpTo(Routes.REGISTER) { inclusive = true }
                            }
                        },
                    )
                }
            }
        }
    }
}
