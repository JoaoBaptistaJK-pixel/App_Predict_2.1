
package com.example.apppredict20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaVencidos(
    viewModel: MeuViewModel,
    onVoltar: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.recarregarProdutos()
    }

    val produtosVencidos = viewModel.produtos.filter { produto ->
        viewModel.verificarValidade(produto) == "Vencido"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produtos vencidos") },

                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PetrolBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = AppBackground
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),

            contentPadding = PaddingValues(20.dp),

            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {
                Column {
                    Text(
                        text = "Produtos vencidos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PetrolBlueDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Confira os produtos que ultrapassaram a validade.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEB)
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
                            tint = ErrorColor,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "${produtosVencidos.size}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorColor
                            )

                            Text(
                                text = "Produtos fora da validade",
                                fontSize = 13.sp,
                                color = ErrorColor
                            )
                        }
                    }
                }
            }

            if (produtosVencidos.isEmpty()) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessColor,
                                modifier = Modifier.size(42.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Nenhum produto vencido!",
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Seu estoque não possui produtos fora da validade.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

            } else {

                items(
                    items = produtosVencidos,
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
                                color = ErrorColor.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                Text(
                                    text = "Vencido",
                                    color = ErrorColor,
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
        }
    }
}
