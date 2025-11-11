package com.example.app_ajudai.feature.inbox.data

import androidx.room.*
import com.example.app_ajudai.feature.auth.data.User
import com.example.app_ajudai.feature.favor.data.Favor

@Entity(
    tableName = "help_request",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["requesterId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["recipientId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Favor::class, parentColumns = ["id"], childColumns = ["favorId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [
        Index("favorId"),
        Index("recipientId"),
        Index("requesterId"),
        Index(value = ["favorId", "requesterId"], unique = true)
    ]
)
data class HelpRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val favorId: Long,
    val requesterId: Long,
    val recipientId: Long,
    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
)

data class HelpRequestWithInfo(
    @Embedded val request: HelpRequest,
    @Relation(parentColumn = "favorId", entityColumn = "id")
    val favor: Favor,
    @Relation(parentColumn = "requesterId", entityColumn = "id")
    val requester: User
)