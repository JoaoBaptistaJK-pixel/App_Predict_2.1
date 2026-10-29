
package com.example.apppredict20

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.Delete

@Dao
interface ProdutoDAO {

    @Insert
    suspend fun cadastrarProduto(produto: Produto)

    @Query("SELECT * FROM produtos")
    suspend fun listarProdutos(): List<Produto>

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Produto?

    @Update
    suspend fun editarProduto(produto: Produto)

    @Delete
    suspend fun excluirProduto(produto: Produto)
}
