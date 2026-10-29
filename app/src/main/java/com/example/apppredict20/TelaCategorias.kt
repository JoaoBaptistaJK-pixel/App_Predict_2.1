
package com.example.apppredict20

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TelaCategorias(
    onVoltar: () -> Unit
) {

    val context = LocalContext.current

    val categoriaDAO = remember(context) {
        AppDatabase.obterBanco(context).categoriaDAO()
    }

    var categorias by remember {
        mutableStateOf(listOf<Categoria>())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var categoriaEditando by remember {
        mutableStateOf<Categoria?>(null)
    }

    var nome by remember {
        mutableStateOf("")
    }

    var descricao by remember {
        mutableStateOf("")
    }

    var erro by remember {
        mutableStateOf("")
    }

    var erroLista by remember {
        mutableStateOf("")
    }

    var salvando by remember {
        mutableStateOf(false)
    }

    fun carregarCategorias() {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = categoriaDAO.listarCategorias()

                withContext(Dispatchers.Main) {
                    categorias = lista
                    carregando = false
                    erroLista = ""
                }

            } catch (e: Exception) {

                Log.e(
                    "AppPredict",
                    "Erro ao buscar categorias",
                    e
                )

                withContext(Dispatchers.Main) {
                    erroLista = "Erro ao carregar categorias."
                    carregando = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        carregarCategorias()
    }

    fun abrirCadastro() {
        categoriaEditando = null
        nome = ""
        descricao = ""
        erro = ""
        mostrarFormulario = true
    }

    fun abrirEdicao(categoria: Categoria) {
        categoriaEditando = categoria
        nome = categoria.nome
        descricao = categoria.descricao
        erro = ""
        mostrarFormulario = true
    }

    // FORMULÁRIO PARA CADASTRAR OU EDITAR
    if (mostrarFormulario) {

        AlertDialog(
            onDismissRequest = {
                if (!salvando) {
                    mostrarFormulario = false
                }
            },

            title = {
                Text(
                    text = if (categoriaEditando == null)
                        "Adicionar categoria"
                    else
                        "Editar categoria",
                    color = PetrolBlueDark,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = nome,
                        onValueChange = {
                            nome = it
                            erro = ""
                        },
                        label = {
                            Text("Nome da categoria")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = descricao,
                        onValueChange = {
                            descricao = it
                        },
                        label = {
                            Text("Descrição")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (erro.isNotEmpty()) {
                        Text(
                            text = erro,
                            color = ErrorColor,
                            fontSize = 13.sp
                        )
                    }
                }
            },

            confirmButton = {

                TextButton(
                    enabled = !salvando,

                    onClick = {

                        val nomeDigitado = nome.trim()
                        val descricaoDigitada = descricao.trim()
                        val categoriaAtual = categoriaEditando

                        if (nomeDigitado.isBlank()) {

                            erro = "Informe o nome da categoria."

                        } else {

                            salvando = true

                            CoroutineScope(Dispatchers.IO).launch {

                                try {

                                    if (categoriaAtual == null) {

                                        categoriaDAO.cadastrarCategoria(
                                            Categoria(
                                                nome = nomeDigitado,
                                                descricao = descricaoDigitada
                                            )
                                        )

                                    } else {

                                        categoriaDAO.editarCategoria(
                                            categoriaAtual.copy(
                                                nome = nomeDigitado,
                                                descricao = descricaoDigitada
                                            )
                                        )
                                    }

                                    val lista =
                                        categoriaDAO.listarCategorias()

                                    withContext(Dispatchers.Main) {

                                        categorias = lista
                                        mostrarFormulario = false
                                        categoriaEditando = null
                                        nome = ""
                                        descricao = ""
                                        erro = ""
                                        salvando = false
                                    }

                                } catch (e: Exception) {

                                    Log.e(
                                        "AppPredict",
                                        "Erro ao salvar categoria",
                                        e
                                    )

                                    withContext(Dispatchers.Main) {
                                        erro = "Erro ao salvar categoria."
                                        salvando = false
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Salvar",
                        color = PetrolBlue
                    )
                }
            },

            dismissButton = {

                TextButton(
                    enabled = !salvando,
                    onClick = {
                        mostrarFormulario = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // TELA PRINCIPAL DE CATEGORIAS
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),

        contentPadding = PaddingValues(20.dp),

        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // CABEÇALHO
        item {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onVoltar
                ) {
                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = PetrolBlue
                    )
                }

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Column {

                    Text(
                        text = "Minhas categorias",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PetrolBlueDark
                    )

                    Text(
                        text = "Organize as categorias cadastradas.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // QUANTIDADE DE CATEGORIAS
        item {

            Text(
                text = "${categorias.size} categorias cadastradas",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        if (erroLista.isNotEmpty()) {
            item {
                Text(
                    text = erroLista,
                    color = ErrorColor
                )
            }
        }

        if (carregando) {

            item {
                CircularProgressIndicator(
                    color = PetrolBlue
                )
            }

        } else if (categorias.isEmpty()) {

            // ESTADO VAZIO
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

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            tint = PetrolBlue,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Nenhuma categoria cadastrada",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Adicione sua primeira categoria.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

        } else {

            // CARTÕES DAS CATEGORIAS
            items(
                items = categorias,
                key = { categoria -> categoria.id }
            ) { categoria ->

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
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = categoria.nome,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PetrolBlueDark
                            )

                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = null,
                                tint = PetrolBlue
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = categoria.descricao.ifBlank {
                                "Sem descrição"
                            },
                            fontSize = 14.sp,
                            color = TextSecondary
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        HorizontalDivider(
                            color = Color(0xFFE5E7EB)
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            TextButton(
                                onClick = {
                                    abrirEdicao(categoria)
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(
                                    modifier = Modifier.width(5.dp)
                                )

                                Text("Editar")
                            }

                            TextButton(
                                onClick = {

                                    CoroutineScope(Dispatchers.IO).launch {

                                        try {

                                            categoriaDAO.excluirCategoria(
                                                categoria
                                            )

                                            val lista =
                                                categoriaDAO.listarCategorias()

                                            withContext(Dispatchers.Main) {
                                                categorias = lista
                                            }

                                        } catch (e: Exception) {

                                            Log.e(
                                                "AppPredict",
                                                "Erro ao excluir categoria",
                                                e
                                            )

                                            withContext(Dispatchers.Main) {
                                                erroLista =
                                                    "Erro ao excluir categoria."
                                            }
                                        }
                                    }
                                },

                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = ErrorColor
                                )
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(
                                    modifier = Modifier.width(5.dp)
                                )

                                Text("Excluir")
                            }
                        }
                    }
                }
            }
        }

        // BOTÃO NO FINAL DA LISTA
        item {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = {
                    abrirCadastro()
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

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Adicionar categoria",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
