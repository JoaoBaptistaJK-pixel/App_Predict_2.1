
package com.example.apppredict20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaEstoque(
    viewModel: MeuViewModel,
    onEditar: (Long) -> Unit,
    onDetalhes: (Long) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Meu estoque",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PetrolBlueDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${viewModel.produtos.size} produtos cadastrados",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = PetrolBlue,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.produtos.isEmpty()) {

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
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Seu estoque está vazio",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Cadastre seu primeiro produto para começar.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

        } else {

            for (produto in viewModel.produtos) {

                val situacao = viewModel.verificarValidade(produto)

                val corSituacao = when (situacao) {
                    "Vencido" -> ErrorColor
                    "Próximo do vencimento" -> WarningColor
                    else -> SuccessColor
                }

                val textoSituacao = when (situacao) {
                    "Próximo do vencimento" -> "Próximo de vencer"
                    else -> situacao
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = produto.nome,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PetrolBlueDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = corSituacao.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = textoSituacao,
                                color = corSituacao,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            color = Color(0xFFE5E7EB)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextButton(
                                onClick = { onDetalhes(produto.id) },
                                contentPadding = PaddingValues(
                                    horizontal = 4.dp,
                                    vertical = 4.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text("Detalhes", fontSize = 12.sp)
                            }

                            TextButton(
                                onClick = { onEditar(produto.id) },
                                contentPadding = PaddingValues(
                                    horizontal = 4.dp,
                                    vertical = 4.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text("Editar", fontSize = 12.sp)
                            }

                            TextButton(
                                onClick = {
                                    viewModel.excluirProduto(produto.id)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = ErrorColor
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 4.dp,
                                    vertical = 4.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text("Excluir", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
