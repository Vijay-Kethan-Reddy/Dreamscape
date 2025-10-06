package com.example.dreamscape.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamscape.Models.DreamCategory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel()  {

    private val _categories = MutableStateFlow<List<DreamCategory>>(emptyList())
    val categories: StateFlow<List<DreamCategory>> = _categories

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            db.collection("categories")
                .get()
                .addOnSuccessListener { result ->
                    val list = result.documents.mapNotNull { doc ->
                        doc.toObject(DreamCategory::class.java)?.copy(id = doc.id)
                    }.filterNotNull()
                    _categories.value = list
                }
                .addOnFailureListener{ e ->
                    e.printStackTrace()
                }

        }
    }
}