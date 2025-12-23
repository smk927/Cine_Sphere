package com.example.cinesphere.domain.usecase

import com.example.cinesphere.data.repository.GenreChatRepository
import com.example.cinesphere.domain.model.DiscussionMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: GenreChatRepository
) {
    operator fun invoke(genreId: String): Flow<List<DiscussionMessage>> {
        return repository.getMessages(genreId)
    }
}