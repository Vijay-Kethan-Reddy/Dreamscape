package com.example.dreamscape.Views

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dreamscape.Models.DreamCategory
import com.example.dreamscape.ViewModels.AuthViewModel
import com.example.dreamscape.ViewModels.DreamFeedViewModel
import com.example.dreamscape.Models.DreamPost
import com.example.dreamscape.ViewModels.CategoriesViewModel
import com.example.dreamscape.ViewModels.PostDreamViewModel
import com.example.dreamscape.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {

    val user by authViewModel.currentUser.collectAsState()
    val categories = categoriesViewModel.categories.collectAsState(emptyList()).value


    LaunchedEffect(user) {
        if (user == null) {
            rootNavController.navigate("auth") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DREAMSCAPE",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding())
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = {
                        val encodedName = URLEncoder.encode(category.name, StandardCharsets.UTF_8.toString())
                        rootNavController.navigate("category/${category.id}/$encodedName")
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(category: DreamCategory, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .padding(8.dp)
            .height(100.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurfaceVariant
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}