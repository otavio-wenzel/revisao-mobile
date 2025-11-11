package com.example.app_ajudai.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore para preferências de sessão (guardar id do usuário logado)
private val Context.dataStore by preferencesDataStore("ajudai_session")

/**
 * Encapsula acesso ao DataStore para manter o userId atual.
 */
class UserSession(private val context: Context) {
    private val KEY_USER_ID = longPreferencesKey("user_id")

    // Flow reativo do id atual (ou null se sem sessão)
    val userIdFlow: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    // Define/limpa o userId na sessão
    suspend fun setUserId(id: Long?) {
        context.dataStore.edit { prefs ->
            if (id == null) prefs.remove(KEY_USER_ID) else prefs[KEY_USER_ID] = id
        }
    }
}
