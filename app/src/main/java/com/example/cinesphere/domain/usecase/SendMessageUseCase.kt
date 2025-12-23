package com.example.cinesphere.domain.usecase

import com.example.cinesphere.data.repository.GenreChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: GenreChatRepository
) {
    suspend operator fun invoke(genreId: String, text: String) {
        if (text.isNotBlank()) {
            repository.sendMessage(genreId, text)
        }
    }
}