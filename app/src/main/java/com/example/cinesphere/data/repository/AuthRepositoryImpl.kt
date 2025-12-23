package com.example.cinesphere.data.repository

import com.example.cinesphere.data.session.UserSession
import com.example.cinesphere.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                // Set display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(email.substringBefore('@'))
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                // Initialize user document
                val user = hashMapOf(
                    "isSubscribed" to false
                )
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // Fetch subscription status on sign in
            val isSubscribed = getSubscriptionStatus()
            UserSession.setSubscribed(isSubscribed)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        UserSession.setSubscribed(false)
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun setSubscriptionStatus(isSubscribed: Boolean) {
        val userId = getUserId() ?: return
        try {
            val data = hashMapOf("isSubscribed" to isSubscribed)
            firestore.collection("users").document(userId)
                .set(data, SetOptions.merge())
                .await()
            // Also update local session
            UserSession.setSubscribed(isSubscribed)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getSubscriptionStatus(): Boolean {
        val userId = getUserId() ?: return false
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            document.getBoolean("isSubscribed") ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
