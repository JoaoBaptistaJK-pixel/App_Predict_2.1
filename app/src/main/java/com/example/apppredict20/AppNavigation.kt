
package com.example.apppredict20

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: MeuViewModel = viewModel()

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
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.EDITAR + "/{id}") { entrada ->
            val id = entrada.arguments?.getString("id")?.toLongOrNull()
            val produto = id?.let { viewModel.buscarProduto(it) }

            if (produto != null) {
                TelaEditar(
                    produto = produto,
                    viewModel = viewModel,
                    onVoltar = { navController.popBackStack() }
                )
            }
        }

        composable(Rotas.VENCIDOS) {
            TelaVencidos(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.DETALHES + "/{id}") { entrada ->
            val id = entrada.arguments?.getString("id")?.toLongOrNull()
            val produto = id?.let { viewModel.buscarProduto(it) }

            if (produto != null) {
                TelaDetalhes(
                    produto = produto,
                    viewModel = viewModel,
                    onVoltar = { navController.popBackStack() },
                    onEditar = {
                        navController.navigate("${Rotas.EDITAR}/${produto.id}")
                    }
                )
            }
        }
    }
}
