package com.example.dreamscape.Repositories

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun logout() {
        auth.signOut()
    }


}