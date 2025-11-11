package com.example.app_ajudai.feature.favor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.feature.favor.data.Favor
import com.example.app_ajudai.feature.favor.data.FavorRepository
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_ajudai.feature.inbox.InboxViewModel
import com.example.app_ajudai.feature.inbox.data.InboxResult

/**
 * Tela de detalhe do Favor. Mostra dados + autor e botão "Quero ajudar!" (se não for do próprio autor).
 * Ao clicar, cria uma HelpRequest para a inbox do autor.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorDetailScreen(
    favorId: Long,
    repo: FavorRepository,
    onNavigateBack: () -> Unit,
    currentUserId: Long?
) {
    // Observa Favor + Autor
    val favorWithUserFlow = remember(favorId) { repo.observarFavorComUsuario(favorId) }
    val favorWithUser by favorWithUserFlow.collectAsStateWithLifecycle(initialValue = null)

    // VM da inbox para registrar interesse
    val context = LocalContext.current
    val inboxVM: InboxViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return InboxViewModel(context.applicationContext as Application) as T
        }
    })

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var alreadyNotified by remember { mutableStateOf(false) } // evita duplo envio

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Favor", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            val data = favorWithUser
            if (data != null) {
                val isOwner = (currentUserId != null) && (currentUserId == data.favor.userId)

                FavorDetailContent(
                    favor = data.favor,
                    authorName = data.user.name,
                    showHelpButton = !isOwner, // esconde botão se for do próprio autor
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    onHelpClick = {
                        val me = currentUserId
                        if (me == null) {
                            scope.launch { snackbarHostState.showSnackbar("Faça login para continuar.") }
                            return@FavorDetailContent
                        }
                        if (alreadyNotified) {
                            scope.launch { snackbarHostState.showSnackbar("Você já enviou interesse para este favor.") }
                            return@FavorDetailContent
                        }
                        // Cria HelpRequest -> inbox do autor
                        inboxVM.requestHelp(
                            favorId = data.favor.id,
                            requesterId = me,
                            recipientId = data.favor.userId
                        ) { res ->
                            scope.launch {
                                when (res) {
                                    is InboxResult.Success -> {
                                        alreadyNotified = true
                                        snackbarHostState.showSnackbar("Interesse enviado ao autor!")
                                    }
                                    is InboxResult.Error -> snackbarHostState.showSnackbar(res.message)
                                }
                            }
                        }
                    }
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erro: Favor não encontrado.", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun FavorDetailContent(
    favor: Favor,
    authorName: String,
    showHelpButton: Boolean,
    modifier: Modifier = Modifier,
    onHelpClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(favor.categoria, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(favor.titulo, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Publicado por $authorName", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            Text(favor.descricao, style = MaterialTheme.typography.bodyLarge)
        }

        if (showHelpButton) {
            Button(onClick = onHelpClick, modifier = Modifier.fillMaxWidth().height(50.dp), shape = MaterialTheme.shapes.medium) {
                Text("Quero Ajudar!", style = MaterialTheme.typography.labelLarge)
            }
        } else {
            Text("Esta é a sua publicação.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth())
        }
    }
}
