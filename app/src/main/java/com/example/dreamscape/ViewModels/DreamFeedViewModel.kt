package com.example.dreamscape.ViewModels

import android.adservices.adid.AdId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamscape.Models.DreamPost
import com.example.dreamscape.Repositories.AuthRepository
import com.example.dreamscape.Repositories.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DreamFeedViewModel @Inject constructor(
    private val dreamRepository: DreamRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _dreams = MutableStateFlow<List<DreamPost>>(emptyList())
    val dreams: StateFlow<List<DreamPost>> = _dreams

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadDreams()
    }

    fun loadDreams() {
        viewModelScope.launch {
            val result: Result<List<DreamPost>> = dreamRepository.getDreams()
            if (result.isSuccess) {
                _dreams.value = result.getOrNull() ?: emptyList()
                _error.value =  null
            } else {
                _dreams.value = emptyList()
                _error.value = result.exceptionOrNull()?.message ?:"Unknown Error"

            }

        }
    }

    fun toggleLikes(dreamId: String, userId: String) {
        val user = authRepository.getCurrentUser() ?: return
        viewModelScope.launch {
            dreamRepository.likeDream(dreamId, userId)
            loadDreams()
        }
    }

    fun toggleSaves(dreamId: String, userId: String) {
        val user = authRepository.getCurrentUser() ?: return
        viewModelScope.launch {
            dreamRepository.saveDream(dreamId, userId)
            loadDreams()
        }
    }

    fun deleteDream(dreamId: String) {
        viewModelScope.launch {
            val result = dreamRepository.deleteDream(dreamId)
            result.onSuccess {
                _dreams.value = _dreams.value.filterNot { it.id == dreamId }
            }.onFailure { e ->
                _error.value = e.message ?: "Failed to delete dream"
            }
        }
    }
}