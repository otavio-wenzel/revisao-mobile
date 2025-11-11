package com.example.app_ajudai.feature.favor.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

/**
 * DAO de Favor: CRUD + consultas observáveis (Flow) e join com User.
 */
@Dao
interface FavorDao {

    @Insert
    suspend fun inserir(favor: Favor): Long

    @Update
    suspend fun atualizar(favor: Favor)

    @Delete
    suspend fun deletar(favor: Favor)

    @Query("SELECT * FROM favor ORDER BY createdAt DESC")
    fun observarTodos(): Flow<List<Favor>>

    @Query("SELECT * FROM favor WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<Favor?>

    @Query("SELECT * FROM favor WHERE userId = :userId ORDER BY createdAt DESC")
    fun observarDoUsuario(userId: Long): Flow<List<Favor>>

    // Favor + autor
    @Transaction
    @Query("SELECT * FROM favor WHERE id = :id LIMIT 1")
    fun observarFavorComUsuario(id: Long): Flow<FavorWithUser?>

    // Busca com categorias
    @Query("""
        SELECT * FROM favor
        WHERE (:query IS NULL OR :query = '' 
           OR titulo LIKE '%' || :query || '%' 
           OR descricao LIKE '%' || :query || '%')
          AND categoria IN (:categorias)
        ORDER BY createdAt DESC
    """)
    fun observarFiltradosComCategorias(
        query: String?,
        categorias: List<String>
    ): Flow<List<Favor>>

    // Busca sem filtro de categoria
    @Query("""
        SELECT * FROM favor
        WHERE (:query IS NULL OR :query = '' 
           OR titulo LIKE '%' || :query || '%' 
           OR descricao LIKE '%' || :query || '%')
        ORDER BY createdAt DESC
    """)
    fun observarFiltradosSemCategoria(query: String?): Flow<List<Favor>>
}
