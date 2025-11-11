package com.example.app_ajudai.feature.favor.data

import androidx.room.*
import com.example.app_ajudai.feature.auth.data.User

/**
 * Entidade Favor: pedido de ajuda, com foreign key para User (autor).
 */
@Entity(
    tableName = "favor",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("userId"),
        Index("categoria"),
        Index("createdAt")
    ]
)
data class Favor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * DTO composto usado para exibir "Publicado por ..."
 */
data class FavorWithUser(
    @Embedded val favor: Favor,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: com.example.app_ajudai.feature.auth.data.User
)
