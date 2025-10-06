package com.example.dreamscape.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamscape.Repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

   private val _currentUser = MutableStateFlow<FirebaseUser?>(authRepository.getCurrentUser())

       val currentUser: StateFlow<FirebaseUser?> = _currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if(result.isSuccess) {
                _currentUser.value = authRepository.getCurrentUser()
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                _currentUser.value = authRepository.getCurrentUser()
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }
}