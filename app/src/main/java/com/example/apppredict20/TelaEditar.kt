
package com.example.apppredict20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaEditar(
    produto: Produto,
    viewModel: MeuViewModel,
    onVoltar: () -> Unit
) {
    var nome by remember(produto.id) {
        mutableStateOf(produto.nome)
    }

    var quantidade by remember(produto.id) {
        mutableStateOf(produto.quantidade.toString())
    }

    var validade by remember(produto.id) {
        mutableStateOf(produto.validade)
    }

    var erro by remember(produto.id) {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar produto") },
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
                text = "Editar informações",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PetrolBlueDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Atualize os dados do produto selecionado.",
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
                            erro = ""
                        },
                        label = { Text("Nome do produto") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = quantidade,
                        onValueChange = {
                            quantidade = it
                            erro = ""
                        },
                        label = { Text("Quantidade") },
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
                            erro = ""
                        },
                        label = { Text("Validade (DD/MM/AAAA)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            if (erro.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = erro,
                    color = ErrorColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val qtd = quantidade.toIntOrNull()

                    val formato = DateTimeFormatter
                        .ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT)

                    val dataValida = try {
                        LocalDate.parse(validade, formato)
                        true
                    } catch (e: DateTimeParseException) {
                        false
                    }

                    if (
                        nome.isBlank() ||
                        qtd == null ||
                        qtd <= 0 ||
                        !dataValida
                    ) {
                        erro = "Verifique nome, quantidade e validade."
                    } else {
                        viewModel.editarProduto(
                            produto.copy(
                                nome = nome.trim(),
                                quantidade = qtd,
                                validade = validade
                            )
                        )

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
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Salvar alterações",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
