package com.example.cinesphere.domain.usecase

import com.example.cinesphere.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signUp(email, password)
    }
}