package com.example.cinesphere.domain.usecase

import com.example.cinesphere.data.session.UserSession
import com.example.cinesphere.domain.repository.AuthRepository
import javax.inject.Inject

class CheckSubscriptionStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        if (authRepository.isUserLoggedIn()) {
            val isSubscribed = authRepository.getSubscriptionStatus()
            UserSession.setSubscribed(isSubscribed)
        }
    }
}