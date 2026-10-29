
package com.example.apppredict20

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: MeuViewModel = viewModel()

    val context = LocalContext.current

    val banco = remember(context) {
        AppDatabase.obterBanco(context)
    }

    val produtoDAO = remember(banco) {
        banco.produtoDAO()
    }

    suspend fun carregarProdutos() {
        try {
            val lista = withContext(Dispatchers.IO) {
                produtoDAO.listarProdutos()
            }

            withContext(Dispatchers.Main) {
                viewModel.atualizarProdutos(lista)
            }

        } catch (e: Exception) {
            Log.e("AppPredict", "Erro ao buscar produtos", e)
        }
    }

    LaunchedEffect(viewModel, produtoDAO) {

        viewModel.configurarPersistencia(

            cadastrar = { produto ->

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        produtoDAO.cadastrarProduto(produto)
                        carregarProdutos()

                    } catch (e: Exception) {
                        Log.e("AppPredict", "Erro ao cadastrar", e)
                    }
                }
            },

            editar = { produto ->

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        produtoDAO.editarProduto(produto)
                        carregarProdutos()

                    } catch (e: Exception) {
                        Log.e("AppPredict", "Erro ao editar", e)
                    }
                }
            },

            excluir = { id ->

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val produto = produtoDAO.buscarPorId(id)

                        if (produto != null) {
                            produtoDAO.excluirProduto(produto)
                            carregarProdutos()
                        }

                    } catch (e: Exception) {
                        Log.e("AppPredict", "Erro ao excluir", e)
                    }
                }
            },

            recarregar = {

                CoroutineScope(Dispatchers.IO).launch {
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
                ?.toIntOrNull()

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
                ?.toIntOrNull()

            if (id != null) {
                TelaDetalhes(
                    produtoId = id,
                    produtoDAO = produtoDAO,
                    viewModel = viewModel,
                    onVoltar = {
                        navController.popBackStack()
                    },
                    onEditar = {
                        navController.navigate(
                            "${Rotas.EDITAR}/$id"
                        )
                    }
                )
            }
        }
    }
}
