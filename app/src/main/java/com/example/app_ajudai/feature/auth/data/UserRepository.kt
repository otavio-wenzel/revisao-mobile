package com.example.app_ajudai.feature.auth.data

import com.example.app_ajudai.core.security.PasswordHasher
import kotlinx.coroutines.flow.Flow

// Resultado padrão para telas de auth
sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface UserRepository {
    suspend fun signUp(name: String, location: String, email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): Pair<AuthResult, Long?> // (resultado, userId)
    fun observeUser(userId: Long): Flow<User?>
    suspend fun userExists(id: Long): Boolean
    suspend fun updateName(userId: Long, newName: String): AuthResult
    suspend fun changePassword(userId: Long, oldPassword: String, newPassword: String): AuthResult
}

/**
 * Implementação Room do repositório de usuários.
 */
class UserRepositoryRoom(private val dao: UserDao) : UserRepository {

    override suspend fun signUp(
        name: String,
        location: String,
        email: String,
        password: String
    ): AuthResult {
        // validações simples de cadastro
        if (name.isBlank() || location.isBlank() || email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Preencha todos os campos.")
        }
        if (dao.getByEmail(email) != null) {
            return AuthResult.Error("E-mail já cadastrado.")
        }

        val hash = PasswordHasher.sha256(password)
        dao.insert(User(name = name, location = location, email = email, passwordHash = hash))
        return AuthResult.Success
    }

    override suspend fun login(email: String, password: String): Pair<AuthResult, Long?> {
        if (email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Informe e-mail e senha.") to null
        }
        val user = dao.getByEmail(email) ?: return AuthResult.Error("Usuário não encontrado.") to null
        val hash = PasswordHasher.sha256(password)
        if (user.passwordHash != hash) return AuthResult.Error("Senha incorreta.") to null

        return AuthResult.Success to user.id
    }

    override fun observeUser(userId: Long): Flow<User?> = dao.observeById(userId)
    override suspend fun userExists(id: Long): Boolean = dao.existsById(id)

    override suspend fun updateName(userId: Long, newName: String): AuthResult {
        val name = newName.trim()
        if (name.isBlank()) return AuthResult.Error("O nome não pode ficar vazio.")
        val rows = dao.updateName(userId, name)
        return if (rows > 0) AuthResult.Success else AuthResult.Error("Não foi possível atualizar o nome.")
    }

    override suspend fun changePassword(
        userId: Long,
        oldPassword: String,
        newPassword: String
    ): AuthResult {
        if (oldPassword.isBlank() || newPassword.isBlank())
            return AuthResult.Error("Informe as senhas.")
        val user = dao.getById(userId) ?: return AuthResult.Error("Usuário não encontrado.")
        val oldHash = PasswordHasher.sha256(oldPassword)
        if (user.passwordHash != oldHash) return AuthResult.Error("Senha atual incorreta.")
        val newHash = PasswordHasher.sha256(newPassword)
        if (newHash == user.passwordHash) return AuthResult.Error("A nova senha deve ser diferente da atual.")
        val rows = dao.updatePasswordHash(userId, newHash)
        return if (rows > 0) AuthResult.Success else AuthResult.Error("Não foi possível alterar a senha.")
    }
}
