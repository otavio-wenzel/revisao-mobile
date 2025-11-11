package com.example.app_ajudai.feature.favor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_ajudai.feature.favor.data.Favor
import com.example.app_ajudai.feature.favor.data.FavorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(private val repo: FavorRepository) : ViewModel() {

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput.asStateFlow()

    private val _appliedQuery = MutableStateFlow<String?>(null)

    private val _selectedCategories = MutableStateFlow(emptySet<String>())
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()

    fun setSearchInput(q: String) { _searchInput.value = q }

    fun toggleCategory(category: String) {
        _selectedCategories.update { set ->
            if (set.contains(category)) set - category else set + category
        }
        applyFilters()
    }

    fun clearFilters() {
        _searchInput.value = ""
        _selectedCategories.value = emptySet()
        _appliedQuery.value = null
    }

    fun applyFilters() {
        val q = _searchInput.value.trim()
        _appliedQuery.value = if (q.isBlank()) null else q
    }

    val searchFavores: StateFlow<List<Favor>> =
        combine(_appliedQuery, _selectedCategories) { q, cats -> q to cats }
            .flatMapLatest { (q, cats) -> repo.observarFiltrados(q, cats) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val feedFavores: StateFlow<List<Favor>> =
        repo.observarTodos()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun criarFavor(userId: Long, titulo: String, descricao: String, categoria: String) =
        viewModelScope.launch {
            repo.inserir(userId, titulo, descricao, categoria)
        }

    fun deletarFavor(favor: Favor) = viewModelScope.launch { repo.deletar(favor) }

    fun observarMeusFavores(userId: Long) = repo.observarDoUsuario(userId)

    fun atualizarFavor(editado: Favor) = viewModelScope.launch { repo.atualizar(editado) }

    fun observarPorId(id: Long) = repo.observarPorId(id)
}