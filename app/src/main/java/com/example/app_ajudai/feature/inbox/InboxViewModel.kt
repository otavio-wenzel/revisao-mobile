package com.example.app_ajudai.feature.inbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_ajudai.core.db.AppDatabase
import com.example.app_ajudai.feature.inbox.data.HelpRequestWithInfo
import com.example.app_ajudai.feature.inbox.data.InboxRepository
import com.example.app_ajudai.feature.inbox.data.InboxRepositoryRoom
import com.example.app_ajudai.feature.inbox.data.InboxResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InboxViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: InboxRepository = InboxRepositoryRoom(AppDatabase.get(app).helpRequestDao())

    fun observeInbox(recipientId: Long): Flow<List<HelpRequestWithInfo>> =
        repo.observeInbox(recipientId)

    fun observeById(id: Long): Flow<HelpRequestWithInfo?> =
        repo.observeById(id)

    fun requestHelp(favorId: Long, requesterId: Long, recipientId: Long, onDone: (InboxResult) -> Unit) {
        viewModelScope.launch { onDone(repo.requestHelp(favorId, requesterId, recipientId)) }
    }

    fun setStatus(id: Long, status: String, onDone: (InboxResult) -> Unit) {
        viewModelScope.launch { onDone(repo.setStatus(id, status)) }
    }
}