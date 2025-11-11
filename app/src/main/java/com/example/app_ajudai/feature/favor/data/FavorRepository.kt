package com.example.app_ajudai.feature.favor.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Abstração do acesso a dados de Favor.
 */
interface FavorRepository {
    fun observarTodos(): Flow<List<Favor>>
    fun observarPorId(id: Long): Flow<Favor?>
    fun observarFiltrados(query: String?, categorias: Set<String>): Flow<List<Favor>>
    suspend fun inserir(userId: Long, titulo: String, descricao: String, categoria: String)
    suspend fun atualizar(favor: Favor)
    suspend fun deletar(favor: Favor)
    fun observarDoUsuario(userId: Long): Flow<List<Favor>> = flowOf(emptyList())
    fun observarFavorComUsuario(id: Long): Flow<FavorWithUser?>
}

/**
 * Implementação Room do repositório de Favores.
 */
class FavorRepositoryRoom(private val dao: FavorDao) : FavorRepository {

    override fun observarTodos() = dao.observarTodos()
    override fun observarPorId(id: Long) = dao.observarPorId(id)

    override fun observarFiltrados(query: String?, categorias: Set<String>) =
        if (categorias.isEmpty()) dao.observarFiltradosSemCategoria(query)
        else dao.observarFiltradosComCategorias(query, categorias.toList())

    override suspend fun inserir(userId: Long, titulo: String, descricao: String, categoria: String) {
        dao.inserir(Favor(userId = userId, titulo = titulo, descricao = descricao, categoria = categoria))
    }

    override suspend fun atualizar(favor: Favor) = dao.atualizar(favor)
    override suspend fun deletar(favor: Favor) = dao.deletar(favor)
    override fun observarDoUsuario(userId: Long) = dao.observarDoUsuario(userId)
    override fun observarFavorComUsuario(id: Long) = dao.observarFavorComUsuario(id)
}
