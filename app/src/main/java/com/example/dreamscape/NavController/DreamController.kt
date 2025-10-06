package com.example.dreamscape.NavController

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dreamscape.Views.AuthScreen
import com.example.dreamscape.Views.PostScreen
import com.example.dreamscape.Views.HomeScreen
import com.example.dreamscape.Views.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController
import com.example.dreamscape.Views.CommentScreen
import com.example.dreamscape.Views.MainBottomNavScreen
import com.example.dreamscape.Views.CategoryScreen

@ExperimentalMaterial3Api
@Composable
fun DreamController() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    val startDestination = if (auth.currentUser != null) {
        "main"
    } else {
        "auth"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable ("splash") {
            SplashScreen(navController)
        }

        composable("auth") {
            AuthScreen(onAuthSuccess = {
                navController.navigate("main"){
                    popUpTo("auth") { inclusive = true}
                }
            })
        }
        composable("main") {
            MainBottomNavScreen(rootNavController = navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("postScreen") {
            PostScreen(
                navController = navController
            )
        }

        composable("comments/{dreamId}") { backStackEntry ->
            val dreamId = backStackEntry.arguments?.getString("dreamId") ?: return@composable
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            CommentScreen(dreamId = dreamId, navController = navController, userId = userId)
        }

        composable("category/{categoryId}/{categoryName}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""

            CategoryScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                rootNavController = navController
            )

        }

    }
}