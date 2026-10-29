
package com.example.apppredict20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaComAbas(
    navController: NavHostController,
    viewModel: MeuViewModel
) {

    val navInterno = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("App Predict")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PetrolBlue,
                    titleContentColor = Color.White
                )
            )
        },

        bottomBar = {
            BottomNavBar(navInterno = navInterno)
        }

    ) { padding ->

        NavHost(
            navController = navInterno,
            startDestination = Rotas.INICIO,
            modifier = Modifier.padding(padding)
        ) {

            // TELA INICIAL
            composable(Rotas.INICIO) {

                TelaInicio(
                    viewModel = viewModel,

                    onCadastrar = {
                        navController.navigate(Rotas.CADASTRO)
                    },

                    onCategorias = {
                        navInterno.navigate("categorias")
                    }
                )
            }

            // TELA DE ESTOQUE
            composable(Rotas.ESTOQUE) {

                TelaEstoque(
                    viewModel = viewModel,

                    onEditar = { id ->
                        navController.navigate(
                            "${Rotas.EDITAR}/$id"
                        )
                    },

                    onDetalhes = { id ->
                        navController.navigate(
                            "${Rotas.DETALHES}/$id"
                        )
                    }
                )
            }

            // TELA DE ALERTAS
            composable(Rotas.ALERTAS) {

                LaunchedEffect(Unit) {
                    viewModel.recarregarProdutos()
                }

                val proximos = viewModel.produtos.filter {
                    viewModel.verificarValidade(it) ==
                            "Próximo do vencimento"
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppBackground),

                    contentPadding = PaddingValues(20.dp),

                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    item {
                        Column {

                            Text(
                                text = "Alertas de validade",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = PetrolBlueDark
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Produtos que precisam de atenção.",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // RESUMO DOS ALERTAS
                    item {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF6E5)
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = WarningColor,
                                    modifier = Modifier.size(32.dp)
                                )

                                Spacer(
                                    modifier = Modifier.width(16.dp)
                                )

                                Column {

                                    Text(
                                        text = "${proximos.size}",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF925C00)
                                    )

                                    Text(
                                        text = "Produtos próximos do vencimento",
                                        fontSize = 13.sp,
                                        color = Color(0xFF925C00)
                                    )
                                }
                            }
                        }
                    }

                    // SEM PRODUTOS EM ALERTA
                    if (proximos.isEmpty()) {

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {

                                Text(
                                    text = "Nenhum produto próximo do vencimento.",
                                    modifier = Modifier.padding(20.dp),
                                    color = TextSecondary
                                )
                            }
                        }

                    } else {

                        // LISTA DOS PRODUTOS EM ALERTA
                        items(
                            items = proximos,
                            key = { produto -> produto.id }
                        ) { produto ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = produto.nome,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = PetrolBlueDark
                                    )

                                    Surface(
                                        color = WarningColor.copy(
                                            alpha = 0.14f
                                        ),
                                        shape = RoundedCornerShape(50.dp)
                                    ) {

                                        Text(
                                            text = "Próximo de vencer",
                                            color = Color(0xFF925C00),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            )
                                        )
                                    }

                                    Text(
                                        text = "Quantidade: ${produto.quantidade} unidades",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )

                                    Text(
                                        text = "Validade: ${produto.validade}",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    // BOTÃO PARA PRODUTOS VENCIDOS
                    item {

                        Button(
                            onClick = {
                                navController.navigate(Rotas.VENCIDOS)
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),

                            shape = RoundedCornerShape(14.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = PetrolBlue
                            )
                        ) {

                            Text(
                                text = "Ver produtos vencidos",
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            // TELA DE CATEGORIAS
            composable("categorias") {

                TelaCategorias(
                    onVoltar = {
                        navInterno.popBackStack()
                    }
                )
            }
        }
    }
}
