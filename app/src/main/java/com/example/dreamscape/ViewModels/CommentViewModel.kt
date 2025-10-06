package com.example.dreamscape.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dreamscape.Models.Comment
import com.example.dreamscape.Repositories.DreamRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val dreamRepository: DreamRepository
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun loadComments(dreamId: String) {
        viewModelScope.launch {
            val result = dreamRepository.getComments(dreamId)
            result.onSuccess { listOfComments ->
                _comments.value = listOfComments
            }
        }
    }

    fun addComment(dreamId: String, userId: String, text: String) {
        viewModelScope.launch {
            dreamRepository.addComment(dreamId, userId, text)
            loadComments(dreamId)
        }
    }

    fun deleteComment(dreamId: String, commentId: String) {
        viewModelScope.launch {
            val result = dreamRepository.deleteComment(dreamId, commentId)
            result.onSuccess {
                loadComments(dreamId)
            }
        }
    }
}
