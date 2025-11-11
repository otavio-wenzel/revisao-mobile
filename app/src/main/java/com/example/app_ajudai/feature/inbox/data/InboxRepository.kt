package com.example.app_ajudai.feature.inbox.data

import kotlinx.coroutines.flow.Flow

sealed class InboxResult {
    object Success : InboxResult()
    data class Error(val message: String) : InboxResult()
}

interface InboxRepository {
    fun observeInbox(recipientId: Long): Flow<List<HelpRequestWithInfo>>
    fun observeById(id: Long): Flow<HelpRequestWithInfo?>
    suspend fun requestHelp(favorId: Long, requesterId: Long, recipientId: Long): InboxResult
    suspend fun setStatus(id: Long, status: String): InboxResult
}

class InboxRepositoryRoom(private val dao: HelpRequestDao) : InboxRepository {
    override fun observeInbox(recipientId: Long) = dao.observeInbox(recipientId)
    override fun observeById(id: Long) = dao.observeById(id)

    override suspend fun requestHelp(
        favorId: Long,
        requesterId: Long,
        recipientId: Long
    ): InboxResult {
        if (requesterId == recipientId) return InboxResult.Error("Você não pode se candidatar ao próprio favor.")
        val id = dao.insert(HelpRequest(favorId = favorId, requesterId = requesterId, recipientId = recipientId))
        return if (id == -1L) InboxResult.Error("Você já demonstrou interesse nesse favor.")
        else InboxResult.Success
    }

    override suspend fun setStatus(id: Long, status: String): InboxResult {
        val ok = dao.updateStatus(id, status)
        return if (ok > 0) InboxResult.Success else InboxResult.Error("Não foi possível atualizar o status.")
    }
}