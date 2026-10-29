
package com.example.apppredict20

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.Delete

@Dao
interface CategoriaDAO {

    @Insert
    suspend fun cadastrarCategoria(categoria: Categoria): Long

    @Query("SELECT * FROM categorias")
    suspend fun listarCategorias(): List<Categoria>

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun buscarPorId(id: Int): Categoria?

    @Update
    suspend fun editarCategoria(categoria: Categoria)

    @Delete
    suspend fun excluirCategoria(categoria: Categoria)
}
