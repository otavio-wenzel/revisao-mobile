package com.example.app_ajudai.feature.inbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.feature.inbox.InboxViewModel
import com.example.app_ajudai.feature.inbox.data.HelpRequestWithInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    userId: Long,
    inboxViewModel: InboxViewModel,
    onNavigateBack: () -> Unit,
    onOpenRequest: (Long) -> Unit
) {
    val items by inboxViewModel.observeInbox(userId).collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Caixa de entrada", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (items.isEmpty()) {
                item { Text("Você ainda não recebeu solicitações.") }
            } else {
                items(items, key = { it.request.id }) { row ->
                    InboxRow(row) { onOpenRequest(row.request.id) }
                }
            }
        }
    }
}

@Composable
private fun InboxRow(data: HelpRequestWithInfo, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick) {
        Column(Modifier.padding(16.dp)) {
            Text("Interessado: ${data.requester.name}", style = MaterialTheme.typography.titleMedium)
            Text("Publicação: ${data.favor.titulo}", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${when (data.request.status) {
                "PENDING" -> "Pendente"
                "ACCEPTED" -> "Aceito"
                "REJECTED" -> "Recusado"
                else -> data.request.status
            }}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}