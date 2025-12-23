package com.example.cinesphere.domain.repository

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signOut()
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
    suspend fun setSubscriptionStatus(isSubscribed: Boolean)
    suspend fun getSubscriptionStatus(): Boolean
}