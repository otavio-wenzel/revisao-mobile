package com.example.app_ajudai.feature.favor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.feature.favor.AppViewModel
import com.example.app_ajudai.core.common.categoriasDeFavor
import com.example.app_ajudai.feature.favor.data.Favor
import androidx.compose.ui.Alignment

/**
 * Tela de gerenciamento do próprio favor: editar e excluir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageMyPostScreen(
    favorId: Long,
    appViewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    // Observa o favor atual
    val favor by appViewModel.observarPorId(favorId).collectAsStateWithLifecycle(initialValue = null)

    // Estados de formulário
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var categoriaMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    // Preenche o formulário quando o favor é carregado
    LaunchedEffect(favor) {
        favor?.let {
            titulo = it.titulo
            descricao = it.descricao
            categoria = it.categoria
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar publicação", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        }
    ) { innerPadding ->
        if (favor == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Favor não encontrado.", color = MaterialTheme.colorScheme.error)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título
            OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, singleLine = true, modifier = Modifier.fillMaxWidth())

            // Categoria (dropdown)
            ExposedDropdownMenuBox(expanded = categoriaMenu, onExpandedChange = { categoriaMenu = !categoriaMenu }, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = categoria, onValueChange = {}, readOnly = true, label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaMenu) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = categoriaMenu, onDismissRequest = { categoriaMenu = false }) {
                    categoriasDeFavor.forEach { cat ->
                        DropdownMenuItem(text = { Text(cat) }, onClick = { categoria = cat; categoriaMenu = false })
                    }
                }
            }

            // Descrição
            OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth().height(150.dp))

            Spacer(Modifier.height(16.dp))

            // Ações
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        val original: Favor = favor!!
                        val editado = original.copy(titulo = titulo.trim(), descricao = descricao.trim(), categoria = categoria)
                        appViewModel.atualizarFavor(editado)
                        onNavigateBack()
                    },
                    enabled = titulo.isNotBlank() && descricao.isNotBlank() && categoria.isNotBlank(),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) { Text("Salvar") }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Excluir") }
            }
        }

        // Diálogo de confirmação de exclusão
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Excluir publicação") },
                text = { Text("Tem certeza que deseja excluir este favor? Essa ação não pode ser desfeita.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        appViewModel.deletarFavor(favor!!)
                        onNavigateBack()
                    }) { Text("Excluir") }
                },
                dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } }
            )
        }
    }
}
