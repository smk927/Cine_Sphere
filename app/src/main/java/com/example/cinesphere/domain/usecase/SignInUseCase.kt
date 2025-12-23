package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signIn(email, password)
    }
}