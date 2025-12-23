package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class SubscribeUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        delay(2000) // Mock payment processing delay
        authRepository.setSubscriptionStatus(true)
    }
}