package com.example.dreamscape.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dreamscape.ViewModels.AuthViewModel
import com.example.dreamscape.ViewModels.ProfileViewModel
import com.example.dreamscape.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    rootNavController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user by authViewModel.currentUser.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    if (user == null) {
        rootNavController.navigate("auth") {
            popUpTo(0) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                ),
                windowInsets = WindowInsets(0)
            )
        },
        contentWindowInsets = WindowInsets(0),
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Button(
                onClick = { authViewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Logout")
            }
        }
    }
}