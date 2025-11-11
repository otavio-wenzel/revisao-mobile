package com.example.app_ajudai.feature.favor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.feature.favor.AppViewModel
import com.example.app_ajudai.core.common.categoriasDeFavor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitarFavorScreen(
    appViewModel: AppViewModel,
    currentUserId: Long,
    onNavigateBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var descricao by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedir um Favor", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Descreva o favor que precisa", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título do pedido") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecione a Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    categoriasDeFavor.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                selectedCategory = categoria
                                isExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição detalhada do favor...") },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            val canPublish = currentUserId != null &&
                    selectedCategory.isNotEmpty() && descricao.isNotEmpty() && titulo.isNotEmpty()

            Button(
                onClick = {
                    appViewModel.criarFavor(
                        userId = currentUserId!!,
                        titulo = titulo,
                        descricao = descricao,
                        categoria = selectedCategory
                    )
                    onNavigateBack()
                },
                enabled = canPublish,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Publicar Pedido", style = MaterialTheme.typography.labelLarge) }
        }
    }
}