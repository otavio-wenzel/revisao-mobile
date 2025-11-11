package com.example.app_ajudai.feature.inbox.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HelpRequestDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(request: HelpRequest): Long  // retorna -1 se já existir (por índice único)

    @Transaction
    @Query("SELECT * FROM help_request WHERE recipientId = :userId ORDER BY createdAt DESC")
    fun observeInbox(userId: Long): Flow<List<HelpRequestWithInfo>>

    @Transaction
    @Query("SELECT * FROM help_request WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<HelpRequestWithInfo?>

    @Query("UPDATE help_request SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String): Int
}