
package com.example.apppredict20

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface ProdutoDAO {

    @Insert
    suspend fun cadastrarProduto(produto: Produto)

    @Query("SELECT * FROM produtos")
    suspend fun listarProdutos(): List<Produto>

    @Update
    suspend fun editarProduto(produto: Produto)

    @Query("DELETE FROM produtos WHERE id = :id")
    suspend fun excluirProduto(id: Long)
}
