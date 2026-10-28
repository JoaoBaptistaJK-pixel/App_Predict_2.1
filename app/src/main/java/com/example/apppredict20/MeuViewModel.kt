
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

    private var aoCadastrar: ((Produto) -> Unit)? = null
    private var aoEditar: ((Produto) -> Unit)? = null
    private var aoExcluir: ((Long) -> Unit)? = null

    fun configurarPersistencia(
        cadastrar: (Produto) -> Unit,
        editar: (Produto) -> Unit,
        excluir: (Long) -> Unit
    ) {
        aoCadastrar = cadastrar
        aoEditar = editar
        aoExcluir = excluir
    }

    fun atualizarProdutos(lista: List<Produto>) {
        produtos = lista
    }

    fun cadastrarProduto(produto: Produto) {
        aoCadastrar?.invoke(produto)
    }

    fun editarProduto(produtoAtualizado: Produto) {
        aoEditar?.invoke(produtoAtualizado)
    }

    fun excluirProduto(id: Long) {
        aoExcluir?.invoke(id)
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

        val diasRestantes = ChronoUnit.DAYS.between(
            hoje,
            dataValidade
        )

        return when {
            diasRestantes < 0 -> "Vencido"
            diasRestantes <= DIAS_ALERTA -> "Próximo do vencimento"
            else -> "Dentro da validade"
        }
    }
}
