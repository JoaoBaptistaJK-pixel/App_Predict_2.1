package com.example.apppredict20

data class Produto(
    val nome: String,
    val quantidade: Int,
    val validade: String,
    val id: Long = System.nanoTime()
)