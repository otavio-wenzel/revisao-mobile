package com.example.app_ajudai.core.security

import java.security.MessageDigest

/**
 * Utilitário simples para hash SHA-256 (armazenamento de senha com hash).
 * Observação: em produção, considere usar um algoritmo com salt e trabalho (BCrypt/Argon2).
 */
object PasswordHasher {
    fun sha256(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
