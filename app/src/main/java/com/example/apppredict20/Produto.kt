
package com.example.apppredict20

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    val nome: String,
    val quantidade: Int,
    val validade: String,

    @PrimaryKey
    val id: Long = System.nanoTime()
)
