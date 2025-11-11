package com.example.app_ajudai.feature.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_ajudai.feature.auth.data.AuthResult
import com.example.app_ajudai.core.db.AppDatabase
import com.example.app_ajudai.feature.auth.data.UserRepositoryRoom
import com.example.app_ajudai.core.session.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Orquestra login, signup e sessão persistida (DataStore).
 */
class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepositoryRoom(AppDatabase.get(app).userDao())
    private val session = UserSession(app)

    // Estado reativo do userId atual (ou null)
    val currentUserId = session.userIdFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** Garante que, se id salvo não existir mais, a sessão é limpa. */
    fun ensureValidSession() {
        viewModelScope.launch {
            val id = currentUserId.value
            if (id != null && !repo.userExists(id)) {
                session.setUserId(null)
            }
        }
    }

    fun signUp(name: String, location: String, email: String, password: String, onDone: (AuthResult) -> Unit) {
        viewModelScope.launch {
            val res = repo.signUp(name, location, email, password)
            onDone(res)
        }
    }

    fun login(email: String, password: String, onDone: (AuthResult) -> Unit) {
        viewModelScope.launch {
            val (res, id) = repo.login(email, password)
            if (res is AuthResult.Success && id != null) {
                session.setUserId(id) // persiste sessão
            }
            onDone(res)
        }
    }

    fun logout() {
        viewModelScope.launch { session.setUserId(null) }
    }

    // Flow<User?> do usuário corrente (ou null se não logado)
    fun observeUser() = currentUserId.value?.let { id ->
        repo.observeUser(id)
    }

    fun updateName(newName: String, onDone: (AuthResult) -> Unit) {
        val id = currentUserId.value ?: return onDone(AuthResult.Error("Sessão expirada."))
        viewModelScope.launch { onDone(repo.updateName(id, newName)) }
    }

    fun changePassword(oldPassword: String, newPassword: String, onDone: (AuthResult) -> Unit) {
        val id = currentUserId.value ?: return onDone(AuthResult.Error("Sessão expirada."))
        viewModelScope.launch { onDone(repo.changePassword(id, oldPassword, newPassword)) }
    }
}
