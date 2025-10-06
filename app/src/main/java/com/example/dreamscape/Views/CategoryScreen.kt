package com.example.dreamscape.Views

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.dreamscape.ViewModels.CategoryDreamsViewModel
import com.example.dreamscape.Views.DreamCard
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@ExperimentalMaterial3Api
@Composable
fun CategoryScreen(
    categoryId: String,
    categoryName: String,
    rootNavController: NavController,
    viewModel: CategoryDreamsViewModel = hiltViewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val dreams by viewModel.dreams.collectAsState()
    val context = LocalContext.current


    val decodedName = remember(categoryName) {
        URLDecoder.decode(categoryName, StandardCharsets.UTF_8.toString())
    }

    LaunchedEffect(categoryId) {
        viewModel.loadDreamsByCategory(categoryId)
    }

    Scaffold (
        topBar = {
            TopAppBar(title = { Text(decodedName) })
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(dreams) { dream ->
                DreamCard(
                    dream = dream,
                    currentUserId = userId,
                    onLikeClick = { id -> viewModel.likeDream(id, userId) },
                    onSaveClick = { id -> viewModel.saveDream(id, userId) },
                    onCommentClick = { dream -> rootNavController.navigate("comments/${dream.id}") },
                    onShareClick = { dream ->
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${dream.title}\n\n${dream.content}")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Share dream via"))
                    },
                    onDeleteClick = { id -> viewModel.deleteDream(id) }
                )
            }
        }

    }
}