package com.example.app_ajudai.app.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_ajudai.feature.favor.AppViewModel
import com.example.app_ajudai.core.db.AppDatabase
import com.example.app_ajudai.feature.favor.data.FavorRepositoryRoom

/**
 * Factory de ViewModel para prover o AppViewModel com suas dependências (DB/Repository).
 * Evita acoplar a Activity ao detalhe de criação do repositório.
 */
class AppViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            // Cria o banco Room e o repositório de Favores
            val db = AppDatabase.get(app)
            val repo = FavorRepositoryRoom(db.favorDao())
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
