package com.example.app_ajudai.feature.inbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.feature.inbox.InboxViewModel
import com.example.app_ajudai.feature.inbox.data.InboxResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxRequestDetailScreen(
    requestId: Long,
    inboxViewModel: InboxViewModel,
    onNavigateBack: () -> Unit
) {
    val data by inboxViewModel.observeById(requestId).collectAsStateWithLifecycle(initialValue = null)
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitação de ajuda", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { innerPadding ->
        val info = data
        if (info == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Solicitação não encontrada.")
            }
            return@Scaffold
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Interessado", style = MaterialTheme.typography.titleMedium)
            Text(info.requester.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                info.requester.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Divider(Modifier.padding(vertical = 8.dp))

            Text("Publicação", style = MaterialTheme.typography.titleMedium)
            Text(info.favor.titulo, style = MaterialTheme.typography.bodyLarge)
            Text(info.favor.descricao, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        inboxViewModel.setStatus(info.request.id, "ACCEPTED") { res ->
                            scope.launch {
                                val msg = if (res is InboxResult.Success)
                                    "Solicitação aceita."
                                else
                                    "Falha ao aceitar."
                                snackbar.showSnackbar(msg)
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) { Text("Aceitar") }

                OutlinedButton(
                    onClick = {
                        inboxViewModel.setStatus(info.request.id, "REJECTED") { res ->
                            scope.launch {
                                val msg = if (res is InboxResult.Success)
                                    "Solicitação recusada."
                                else
                                    "Falha ao recusar."
                                snackbar.showSnackbar(msg)
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) { Text("Recusar") }
            }

            Text(
                "Status atual: " + when (info.request.status) {
                    "PENDING" -> "Pendente"
                    "ACCEPTED" -> "Aceito"
                    "REJECTED" -> "Recusado"
                    else -> info.request.status
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}