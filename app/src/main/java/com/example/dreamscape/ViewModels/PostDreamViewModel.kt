package com.example.dreamscape.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamscape.Models.DreamPost
import com.example.dreamscape.Repositories.AuthRepository
import com.example.dreamscape.Repositories.DreamRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PostDreamViewModel @Inject constructor(
    private val dreamRepository: DreamRepository,
    private val authRepository: AuthRepository,
    private val db: FirebaseFirestore
): ViewModel() {

    fun postDream(title: String, content: String, categoryId: String, onSuccess: (DreamPost) -> Unit) {
        val user = authRepository.getCurrentUser() ?: return
        val dream = DreamPost(
            userId = user.uid,
            title = title,
            content = content,
            categoryId = categoryId
        )

        viewModelScope.launch {
            val result = dreamRepository.addDreams(dream)
            if (result.isSuccess) {
                onSuccess(dream)
            }
        }
    }
}