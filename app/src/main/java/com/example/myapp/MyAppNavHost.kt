package com.example.myapp

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapp.auth.LoginScreen
import com.example.myapp.core.data.UserPreferences
import com.example.myapp.core.data.remote.Api
import com.example.myapp.core.ui.UserPreferencesViewModel
import com.example.myapp.painting_manager.ui.PaintingScreen
import com.example.myapp.painting_manager.ui.paintings.PaintingsScreen

val paintingsRoute = "paintings"
val authRoute = "auth"

@Composable
fun MyAppNavHost() {
    val navController = rememberNavController()
    val onClosePainting = {
        Log.d("MyAppNavHost", "navigate back to list")
        navController.popBackStack()
    }
    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )
    val myAppViewModel = viewModel<MyAppViewModel>(factory = MyAppViewModel.Factory)
    NavHost(
        navController = navController,
        startDestination = authRoute
    ) {
        composable(paintingsRoute) {
            PaintingsScreen(
                onPaintingClick = { paintingId ->
                    Log.d("MyAppNavHost", "navigate to painting $paintingId")
                    navController.navigate("$paintingsRoute/$paintingId")
                },
                onAddPainting = {
                    Log.d("MyAppNavHost", "navigate to new painting")
                    navController.navigate("$paintingsRoute-new")
                },
                onLogout = {
                    Log.d("MyAppNavHost", "logout")
                    myAppViewModel.logout()
                    Api.tokenInterceptor.token = null
                    navController.navigate(authRoute) {
                        popUpTo(0)
                    }
                })
        }
        composable(
            route = "$paintingsRoute/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        )
        {
            PaintingScreen(
                paintingId = it.arguments?.getString("id"),
                onClose = { onClosePainting() }
            )
        }
        composable(route = "$paintingsRoute-new")
        {
            PaintingScreen(
                paintingId = null,
                onClose = { onClosePainting() }
            )
        }
        composable(route = authRoute)
        {
            LoginScreen(
                onClose = {
                    Log.d("MyAppNavHost", "navigate to list")
                    navController.navigate(paintingsRoute)
                }
            )
        }
    }
    LaunchedEffect(userPreferencesUiState.token) {
        if (userPreferencesUiState.token.isNotEmpty()) {
            Log.d("MyAppNavHost", "Lauched effect navigate to paintings")
            Api.tokenInterceptor.token = userPreferencesUiState.token
            myAppViewModel.setToken(userPreferencesUiState.token)
            navController.navigate(paintingsRoute) {
                popUpTo(0)
            }
        }
    }
}
