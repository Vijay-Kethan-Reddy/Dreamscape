package com.example.dreamscape.Views

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dreamscape.ViewModels.AuthViewModel

@Composable
fun SplashScreen(
navController: NavController,
authViewModel: AuthViewModel = hiltViewModel()
){ val user by authViewModel.currentUser.collectAsState()

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true}
            }
        } else {
            navController.navigate("auth") {
                popUpTo("splash") { inclusive = true}
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}