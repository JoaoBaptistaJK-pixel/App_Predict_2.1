package com.example.apppredict20

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.format.DateTimeFormatter

class MeuViewModel : ViewModel() {

    var produtos by mutableStateOf(listOf<Produto>())
        private set

    fun cadastrarProduto(produto: Produto) {
        produtos = produtos + produto
    }

    fun editarProduto(produtoAtualizado: Produto) {
        produtos = produtos.map { produto ->
            if (produto.id == produtoAtualizado.id) {
                produtoAtualizado
            } else {
                produto
            }
        }
    }

    fun excluirProduto(id: Long) {
        produtos = produtos.filter { it.id != id }
    }

    fun buscarProduto(id: Long): Produto? {
        return produtos.find { it.id == id }
    }

    companion object {
        const val DIAS_ALERTA = 7
    }

    fun verificarValidade(produto: Produto): String {

        val formato = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        val dataValidade = LocalDate.parse(produto.validade, formato)
        val hoje = LocalDate.now()

        val diasRestantes = ChronoUnit.DAYS.between(hoje, dataValidade)

        return when {
            diasRestantes < 0 -> "Vencido"
            diasRestantes <= DIAS_ALERTA -> "Próximo do vencimento"
            else -> "Dentro da validade"
        }
    }

}