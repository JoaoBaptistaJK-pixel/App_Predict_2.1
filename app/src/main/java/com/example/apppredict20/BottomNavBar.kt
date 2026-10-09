package com.example.apppredict20

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navInterno: NavHostController) {

    val backStackEntry by navInterno.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = rotaAtual == Rotas.INICIO,
            onClick = { navInterno.navigate(Rotas.INICIO) },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Início")
            },
            label = { Text("Início") }
        )

        NavigationBarItem(
            selected = rotaAtual == Rotas.ESTOQUE,
            onClick = { navInterno.navigate(Rotas.ESTOQUE) },
            icon = {
                Icon(Icons.Default.List, contentDescription = "Estoque")
            },
            label = { Text("Estoque") }
        )

        NavigationBarItem(
            selected = rotaAtual == Rotas.ALERTAS,
            onClick = { navInterno.navigate(Rotas.ALERTAS) },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = "Alertas")
            },
            label = { Text("Alertas") }
        )
    }
}