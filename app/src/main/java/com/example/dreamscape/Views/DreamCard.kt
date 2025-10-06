package com.example.dreamscape.Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dreamscape.Models.DreamPost
import com.example.dreamscape.ui.theme.*

@Composable
fun DreamCard(
    dream: DreamPost,
    currentUserId: String,
    onLikeClick: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    onCommentClick: (DreamPost) -> Unit,
    onShareClick: (DreamPost) -> Unit,
    onDeleteClick: (String) -> Unit

) {

    var expanded by remember { mutableStateOf(false) }
    var localDream by remember { mutableStateOf(dream) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCommentClick(dream) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        )

    ) {
        Column (modifier = Modifier.padding(16.dp)) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dream.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary

                )

                if(dream.userId == currentUserId) {
                    IconButton(onClick = {onDeleteClick(dream.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete dream",
                            tint = ErrorRed
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = localDream.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                color = TextSecondary
            )

            if (localDream.content.length > 120) {
                Text(
                    text = if (expanded) "Show less" else "Read more",
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimaryBlue,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { expanded = !expanded }
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    IconButton(onClick = { onLikeClick(localDream.id)
                        localDream = localDream.copy(
                            likes = if (localDream.likes.contains(currentUserId)) {
                                localDream.likes - currentUserId
                            } else {
                                localDream.likes + currentUserId
                            }
                        )

                    }) {
                        Icon(
                            imageVector = if(localDream.likes.contains(currentUserId)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Unlike",
                            tint = if(localDream.likes.contains(currentUserId)) ErrorRed else TextTertiary
                        )
                    }

                    Text("${localDream.likes.size}", style = MaterialTheme.typography.labelMedium, color = TextSecondary)

                }


                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { onCommentClick(localDream) }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Comment",
                            tint = TextTertiary
                        )
                    }
                    Text("${localDream.commentCount}", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val isSaved = remember { mutableStateOf(localDream.saves.contains((currentUserId))) }

                    IconButton(onClick = {

                        onSaveClick(localDream.id)

                        localDream = localDream.copy(
                            saves = if (isSaved.value) {
                                localDream.saves - currentUserId
                            } else {
                                localDream.saves + currentUserId
                            }
                        )
                        isSaved.value = !isSaved.value
                    }) {
                        Icon(
                            imageVector = if (isSaved.value) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Save",
                            tint = if (isSaved.value) WarningYellow else TextTertiary
                        )
                    }
                }

                Row(modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { onShareClick(localDream)}) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = TextTertiary
                        )
                    }
                }
            }
        }
    }
}