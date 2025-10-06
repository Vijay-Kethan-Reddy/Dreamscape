package com.example.dreamscape.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamscape.Models.Comment
import com.example.dreamscape.Models.DreamPost
import com.example.dreamscape.Repositories.AuthRepository
import com.example.dreamscape.Repositories.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dreamRepository: DreamRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    val userId: String? get() = authRepository.getCurrentUser()?.uid

    private val _myPosts = MutableStateFlow<List<DreamPost>>(emptyList())
    val myPosts: StateFlow<List<DreamPost>> = _myPosts

    private val _likedPosts = MutableStateFlow<List<DreamPost>>(emptyList())
    val likedPosts: StateFlow<List<DreamPost>> = _likedPosts

    private val _savedPosts = MutableStateFlow<List<DreamPost>>(emptyList())
    val savedPosts: StateFlow<List<DreamPost>> = _savedPosts

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun loadMyPosts() {
        viewModelScope.launch {
            userId?.let { uid ->
                val posts = dreamRepository.getDreams().getOrDefault(emptyList())
                _myPosts.value = posts.filter { it.userId == uid }
            }
        }
    }

    fun loadLikedPosts() {
        viewModelScope.launch {
            userId?.let { uid ->
                val posts = dreamRepository.getDreams().getOrDefault(emptyList())
                _likedPosts.value = posts.filter { it.likes.contains(uid) }
            }
        }
    }

    fun loadSavedPosts() {
        viewModelScope.launch {
            userId?. let { uid ->
                val posts = dreamRepository.getDreams().getOrDefault(emptyList())
                _savedPosts.value =  posts.filter { it.saves.contains(uid) }

            }
        }
    }

    fun loadComments(dreamId: String) {
        viewModelScope.launch {
            val result = dreamRepository.getComments(dreamId)

            }
        }

    fun likeDream(dreamId: String) {
        val uid = userId ?: return
        viewModelScope.launch {
            dreamRepository.likeDream(dreamId, uid)
            loadLikedPosts()
            loadMyPosts()
        }
    }

    fun saveDream(dreamId: String) {
        val uid = userId ?: return
        viewModelScope.launch {
            dreamRepository.saveDream(dreamId, uid)
            loadSavedPosts()
        }
    }

    fun loadUserComments() {
        viewModelScope.launch {
            val allDreams = dreamRepository.getDreams().getOrDefault(emptyList())
            val userComments = mutableListOf<Comment>()
            allDreams.forEach { dream ->
                val commentsResult = dreamRepository.getComments(dream.id)
                val commentsList = commentsResult.getOrDefault(emptyList())
                userComments.addAll(commentsList.filter { it.userId == userId })
            }
            _comments.value = userComments

        }
    }
}

