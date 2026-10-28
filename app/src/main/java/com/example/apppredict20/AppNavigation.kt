
package com.example.apppredict20

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: MeuViewModel = viewModel()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val banco = remember {
        AppDatabase.obterBanco(context)
    }

    val produtoDAO = remember(banco) {
        banco.produtoDAO()
    }

    // Consulta os produtos armazenados no Room
    suspend fun carregarProdutos() {
        val lista = withContext(Dispatchers.IO) {
            produtoDAO.listarProdutos()
        }

        viewModel.atualizarProdutos(lista)
    }

    // Liga as operações do ViewModel ao Room
    LaunchedEffect(viewModel, produtoDAO) {

        viewModel.configurarPersistencia(

            cadastrar = { produto ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        produtoDAO.cadastrarProduto(produto)
                    }
                    carregarProdutos()
                }
            },

            editar = { produto ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        produtoDAO.editarProduto(produto)
                    }
                    carregarProdutos()
                }
            },

            excluir = { id ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        produtoDAO.excluirProduto(id)
                    }
                    carregarProdutos()
                }
            }
        )

        carregarProdutos()
    }

    NavHost(
        navController = navController,
        startDestination = "abas"
    ) {

        composable("abas") {
            TelaComAbas(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Rotas.CADASTRO) {
            TelaCadastro(
                viewModel = viewModel,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rotas.EDITAR + "/{id}") { entrada ->

            val id = entrada.arguments
                ?.getString("id")
                ?.toLongOrNull()

            val produto = id?.let {
                viewModel.buscarProduto(it)
            }

            if (produto != null) {
                TelaEditar(
                    produto = produto,
                    viewModel = viewModel,
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Rotas.VENCIDOS) {
            TelaVencidos(
                viewModel = viewModel,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rotas.DETALHES + "/{id}") { entrada ->

            val id = entrada.arguments
                ?.getString("id")
                ?.toLongOrNull()

            val produto = id?.let {
                viewModel.buscarProduto(it)
            }

            if (produto != null) {
                TelaDetalhes(
                    produto = produto,
                    viewModel = viewModel,
                    onVoltar = {
                        navController.popBackStack()
                    },
                    onEditar = {
                        navController.navigate(
                            "${Rotas.EDITAR}/${produto.id}"
                        )
                    }
                )
            }
        }
    }
}
