
package com.example.apppredict20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastro(
    viewModel: MeuViewModel,
    onVoltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar produto") },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Novo produto",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PetrolBlueDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Preencha as informações para adicionar ao estoque.",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Informações do produto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PetrolBlueDark
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = nome,
                        onValueChange = {
                            nome = it
                            mensagemErro = ""
                        },
                        label = { Text("Nome do produto") },
                        placeholder = { Text("Ex.: Arroz") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = quantidade,
                        onValueChange = {
                            quantidade = it
                            mensagemErro = ""
                        },
                        label = { Text("Quantidade") },
                        placeholder = { Text("Ex.: 5") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = validade,
                        onValueChange = {
                            validade = it
                            mensagemErro = ""
                        },
                        label = { Text("Validade (DD/MM/AAAA)") },
                        placeholder = { Text("Ex.: 20/10/2026") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            if (mensagemErro.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = mensagemErro,
                    color = ErrorColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val quantidadeNumero = quantidade.toIntOrNull()

                    val formato = DateTimeFormatter
                        .ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT)

                    val dataValida = try {
                        LocalDate.parse(validade, formato)
                        true
                    } catch (e: DateTimeParseException) {
                        false
                    }

                    if (nome.isBlank()) {
                        mensagemErro = "Digite o nome do produto."
                    } else if (
                        quantidadeNumero == null ||
                        quantidadeNumero <= 0
                    ) {
                        mensagemErro = "Digite uma quantidade válida."
                    } else if (!dataValida) {
                        mensagemErro = "Digite uma data válida (DD/MM/AAAA)."
                    } else {
                        val produto = Produto(
                            nome = nome.trim(),
                            quantidade = quantidadeNumero,
                            validade = validade
                        )

                        viewModel.cadastrarProduto(produto)
                        onVoltar()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PetrolBlue
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Cadastrar produto",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
