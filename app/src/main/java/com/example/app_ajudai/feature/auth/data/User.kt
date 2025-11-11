package com.example.app_ajudai.feature.auth.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Usuário do sistema. E-mail único via índice.
 */
@Entity(
    tableName = "user",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,      // cidade/bairro ou descrição curta
    val email: String,
    val passwordHash: String,  // senha com hash (não armazenar em texto puro)
    val createdAt: Long = System.currentTimeMillis()
)
