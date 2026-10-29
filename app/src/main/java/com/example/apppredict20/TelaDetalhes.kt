
package com.example.apppredict20

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhes(
    produtoId: Int,
    produtoDAO: ProdutoDAO,
    viewModel: MeuViewModel,
    onVoltar: () -> Unit,
    onEditar: () -> Unit
) {

    var produtoAtual by remember(produtoId) {
        mutableStateOf<Produto?>(null)
    }

    var carregando by remember(produtoId) {
        mutableStateOf(true)
    }

    var erro by remember(produtoId) {
        mutableStateOf(false)
    }

    LaunchedEffect(produtoId, viewModel.produtos) {
        try {
            produtoAtual = withContext(Dispatchers.IO) {
                produtoDAO.buscarPorId(produtoId)
            }

            erro = false

        } catch (e: Exception) {
            Log.e("AppPredict", "Erro ao buscar detalhes", e)
            erro = true

        } finally {
            carregando = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do produto") },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                text = "Informações do produto",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PetrolBlueDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            val produto = produtoAtual

            if (carregando) {

                CircularProgressIndicator(
                    color = PetrolBlue
                )

            } else if (erro) {

                Text(
                    text = "Erro ao consultar o produto no banco.",
                    color = ErrorColor
                )

            } else if (produto == null) {

                Text(
                    text = "Produto não encontrado.",
                    color = TextSecondary
                )

            } else {

                val situacao = viewModel.verificarValidade(produto)

                val corSituacao = when (situacao) {
                    "Vencido" -> ErrorColor
                    "Próximo do vencimento" -> WarningColor
                    else -> SuccessColor
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = PetrolBlue,
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = produto.nome,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PetrolBlueDark
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Quantidade em estoque",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Text(
                            text = "${produto.quantidade} unidades",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Data de validade",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Text(
                            text = produto.validade,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Situação da validade",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = corSituacao.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = situacao,
                                color = corSituacao,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 8.dp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onEditar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PetrolBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Editar produto",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
