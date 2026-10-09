
package com.example.apppredict20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
                title = { Text("App Predict") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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

            composable(Rotas.INICIO) {
                TelaInicio(
                    viewModel = viewModel,
                    onCadastrar = {
                        navController.navigate(Rotas.CADASTRO)
                    }
                )
            }

            composable(Rotas.ESTOQUE) {
                TelaEstoque(
                    viewModel = viewModel,
                    onEditar = { id ->
                        navController.navigate("${Rotas.EDITAR}/$id")
                    },
                    onDetalhes = { id ->
                        navController.navigate("${Rotas.DETALHES}/$id")
                    }
                )
            }

            composable(Rotas.ALERTAS) {

                val produtosProximos = viewModel.produtos.filter { produto ->
                    viewModel.verificarValidade(produto) == "Próximo do vencimento"
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppBackground)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {

                    Text(
                        text = "Alertas de validade",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PetrolBlueDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Acompanhe os produtos que precisam de atenção.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
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
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "${produtosProximos.size}",
                                    fontSize = 32.sp,
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

                    Spacer(modifier = Modifier.height(20.dp))

                    if (produtosProximos.isEmpty()) {

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

                    } else {

                        for (produto in produtosProximos) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(18.dp)
                                ) {

                                    Text(
                                        text = produto.nome,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = PetrolBlueDark
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Surface(
                                        color = WarningColor.copy(alpha = 0.14f),
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

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = "Quantidade: ${produto.quantidade} unidades",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Validade: ${produto.validade}",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

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

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
