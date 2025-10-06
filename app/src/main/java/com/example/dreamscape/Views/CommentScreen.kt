package com.example.dreamscape.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dreamscape.ViewModels.CommentViewModel
import com.example.dreamscape.Models.Comment
import com.example.dreamscape.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CommentScreen(
    dreamId: String,
    navController: NavController,
    userId: String,
    viewModel: CommentViewModel = hiltViewModel()
) {
    val comments by viewModel.comments.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }

    LaunchedEffect(dreamId) {
        viewModel.loadComments(dreamId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Comments",
            style = MaterialTheme.typography.headlineSmall,
            color = PrimaryBlue,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(comments) { comment: Comment ->
                androidx.compose.material3.Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = androidx.compose.material3.CardDefaults.cardElevation(2.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = DarkSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        // Header row with user and delete button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "User: ${comment.userId}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                modifier = Modifier.weight(1f)
                            )

                            if (comment.userId == userId) {
                                IconButton(
                                    onClick = { viewModel.deleteComment(dreamId, comment.commentId) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete comment",
                                        tint = ErrorRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = comment.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatTimestamp(comment.timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextTertiary
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding()
                )
                .background(DarkBackground)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newComment,
                onValueChange = { newComment = it },
                modifier = Modifier.weight(1f).height(56.dp),
                placeholder = { Text("Write a comment...", color = TextTertiary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = InputBorder,
                    cursorColor = PrimaryBlue,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = InputFieldBackground,
                    unfocusedContainerColor = InputFieldBackground
                )
            )
            Button(
                onClick = {
                    if (newComment.isNotBlank() && !isPosting) {
                        isPosting = true
                        viewModel.addComment(dreamId, userId, newComment.trim())
                        newComment = ""
                        isPosting = false
                    }
                },
                enabled = !isPosting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                )
            ) {
                Text(if (isPosting) "Posting..." else "Post")
            }
        }
    }
}

// Helper function to format timestamp in a readable way
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}