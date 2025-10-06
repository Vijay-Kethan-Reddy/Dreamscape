package com.example.dreamscape.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamscape.Models.DreamPost
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class CategoryDreamsViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _dreams = MutableStateFlow<List<DreamPost>>(emptyList())
    val dreams: StateFlow<List<DreamPost>> = _dreams

    private var currentCategoryId: String = "" // 🔹 add this


    fun loadDreamsByCategory(categoryId: String) {
        currentCategoryId = categoryId
        viewModelScope.launch {
            try {
            val snapshot = db.collection("dreams")
                .whereEqualTo("categoryId", categoryId)
                .get()
                .await()
                    val list = snapshot.documents.mapNotNull { it.toObject(DreamPost::class.java) }
                    _dreams.value = list
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }
    }

    fun likeDream(dreamId: String, userId: String) {
        val dreamRef = db.collection("dreams").document(dreamId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(dreamRef)
            val likes = snapshot.get("likes") as? List<String> ?: emptyList()
            val newLikes = if (likes.contains(userId)) {
                likes - userId
            } else {
                likes + userId
            }
            transaction.update(dreamRef, "likes", newLikes)
        }
    }

    fun saveDream(dreamId: String, userId: String) {
        val dreamRef = db.collection("dreams").document(dreamId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(dreamRef)
            val saves = snapshot.get("saves") as? List<String> ?: emptyList()
            val newSaves = if (saves.contains(userId)) {
                saves - userId
            } else {
                saves + userId
            }
            transaction.update(dreamRef, "saves", newSaves)
        }
    }

    fun addDream(dream: DreamPost) {
        if (dream.categoryId == currentCategoryId) {
            _dreams.value = listOf(dream) + _dreams.value
        }
    }

    fun deleteDream(dreamId: String) {
        viewModelScope.launch {
            try {
                val dreamRef = db.collection("dreams").document(dreamId)

                dreamRef.delete().await()

                val commentsSnapshot = dreamRef.collection("comments").get().await()
                for (doc in commentsSnapshot.documents) {
                    doc.reference.delete().await()
                }

                _dreams.value = _dreams.value.filterNot { it.id == dreamId }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}