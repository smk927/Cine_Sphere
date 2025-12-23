package com.example.cinesphere.data.repository

import com.example.cinesphere.domain.model.DiscussionMessage
import com.example.cinesphere.domain.model.GenreRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val usersCollection = firestore.collection("users")
    private val genresCollection = firestore.collection("genres")

    suspend fun joinGenre(genreId: String) {
        val userId = auth.currentUser?.uid ?: return
        usersCollection.document(userId)
            .collection("joined_genres")
            .document(genreId)
            .set(mapOf("joinedAt" to System.currentTimeMillis()))
            .await()
    }

    suspend fun leaveGenre(genreId: String) {
        val userId = auth.currentUser?.uid ?: return
        usersCollection.document(userId)
            .collection("joined_genres")
            .document(genreId)
            .delete()
            .await()
    }

    suspend fun getJoinedGenres(): List<String> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = usersCollection.document(userId)
                .collection("joined_genres")
                .get()
                .await()
            snapshot.documents.map { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // In a real app, these would be fetched from a collection or defined statically
    // For this example, we'll return a static list of available genres
    fun getAvailableGenres(): List<GenreRoom> {
        return listOf(
            GenreRoom("action", "Action"),
            GenreRoom("horror", "Horror"),
            GenreRoom("comedy", "Comedy"),
            GenreRoom("drama", "Drama"),
            GenreRoom("scifi", "Sci-Fi"),
            GenreRoom("romance", "Romance")
        )
    }
    
    fun getGenreRoom(genreId: String): GenreRoom? {
        return getAvailableGenres().find { it.id == genreId }
    }

    suspend fun sendMessage(genreId: String, text: String) {
        val currentUser = auth.currentUser ?: return
        val displayName = currentUser.displayName 
            ?: currentUser.email?.substringBefore('@') 
            ?: "Anonymous"
            
        val message = DiscussionMessage(
            senderId = currentUser.uid,
            senderName = displayName,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        
        genresCollection.document(genreId)
            .collection("messages")
            .add(message)
            .await()
    }

    suspend fun deleteMessage(genreId: String, messageId: String) {
        genresCollection.document(genreId)
            .collection("messages")
            .document(messageId)
            .delete()
            .await()
    }

    fun getMessages(genreId: String): Flow<List<DiscussionMessage>> = callbackFlow {
        val subscription = genresCollection.document(genreId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(DiscussionMessage::class.java)?.copy(id = doc.id)
                    }
                    trySend(messages)
                }
            }

        awaitClose { subscription.remove() }
    }
}
