package com.example.dreamscape.Repositories

import com.example.dreamscape.Models.Comment
import com.example.dreamscape.Models.DreamPost
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class DreamRepository @Inject constructor (private val db: FirebaseFirestore) {
    private val dreamsCollection = db.collection("dreams")
    suspend fun addDreams(dream: DreamPost): Result<Unit> {
        return try {
            val docRef = dreamsCollection.document()
            val newDream = dream.copy(id = docRef.id, commentCount = 0)
            docRef.set(newDream).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getDreams(): Result<List<DreamPost>> {
        return try {
            val snapshot = dreamsCollection
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            val dreams = snapshot.toObjects(DreamPost::class.java)
            Result.success(dreams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeDream(dreamId: String, userId: String): Result<Unit> {
        return try{
            val docRef = dreamsCollection.document(dreamId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                val likes = snapshot.get("likes") as? List<String> ?: emptyList()
                val updatedLikes = if (likes.contains(userId)) {
                    likes - userId
                } else {
                    likes + userId
                }
                transaction.update(docRef, "likes", updatedLikes)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveDream(dreamId: String, userId: String): Result<Unit> {
        return try{
            val docRef = dreamsCollection.document(dreamId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                val saves = snapshot.get("saves") as? List<String> ?: emptyList()
                val updatedSaves = if (saves.contains(userId)) {
                    saves - userId
                } else {
                    saves + userId
                }
                transaction.update(docRef, "saves", updatedSaves)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addComment(dreamId: String, userId: String, text: String): Result<Unit> {
        return try{
            val commentData = mapOf(
                    "userId" to userId,
                    "text" to text,
                    "timestamp" to com.google.firebase.Timestamp.now()
                )

                dreamsCollection
                    .document(dreamId)
                    .collection("comments")
                    .add(commentData)
                    .await()

            val dreamRef = dreamsCollection.document(dreamId)
            dreamRef.update("commentCount", com.google.firebase.firestore.FieldValue.increment(1)).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComments(dreamId: String): Result<List<Comment>> {
        return try{
            val snapshot = dreamsCollection.document(dreamId)
                .collection("comments")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .await()

            val comments = snapshot.documents.map { doc ->
                Comment(
                    commentId = doc.id,
                    dreamId = dreamId,
                    userId = doc.getString("userId") ?: "",
                    text = doc.getString("text") ?: "",
                    timestamp = doc.getTimestamp("timestamp")?.toDate()?.time ?: 0L
                )
            }
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDreamsByCategory(categoryId: String): Result<List<DreamPost>> {
        return try {
            val snapshot = dreamsCollection
                .whereEqualTo("categoryId", categoryId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            val dreams = snapshot.toObjects(DreamPost::class.java)
            Result.success(dreams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDream(dreamId: String): Result<Unit> {
        return try {
            dreamsCollection.document(dreamId).delete().await()
            val commentsSnapshot = dreamsCollection.document(dreamId)
                .collection("comments")
                .get()
                .await()
            for (doc in commentsSnapshot.documents) {
                doc.reference.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(dreamId: String, commentId: String): Result<Unit> {
        return try {
            dreamsCollection.document(dreamId)
                .collection("comments")
                .document(commentId)
                .delete()
                .await()

            val dreamRef = dreamsCollection.document(dreamId)
            dreamRef.update("commentCount", com.google.firebase.firestore.FieldValue.increment(-1)).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}