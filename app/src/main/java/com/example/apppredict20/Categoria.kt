
package com.example.apppredict20

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    val nome: String,
    val descricao: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
