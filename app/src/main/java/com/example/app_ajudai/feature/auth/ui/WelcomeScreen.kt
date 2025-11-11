package com.example.app_ajudai.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.example.app_ajudai.feature.auth.AuthViewModel
import com.example.app_ajudai.R

/**
 * Tela inicial. Se já houver userId na sessão, redireciona automaticamente.
 */
@Composable
fun WelcomeScreen(
    authViewModel: AuthViewModel,
    onGoLogin: () -> Unit,
    onGoSignUp: () -> Unit,
    onAutoForwardToMain: () -> Unit
) {
    val currentUserId by authViewModel.currentUserId.collectAsState(initial = null)

    // Se já logado, aciona callback para ir ao "main"
    LaunchedEffect(currentUserId) {
        if (currentUserId != null) onAutoForwardToMain()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ajudai_transparente),
            contentDescription = "Logo Ajudaí",
            modifier = Modifier.height(100.dp)
        )

        Spacer(Modifier.height(15.dp))

        Button(onClick = onGoLogin, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Entrar") }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onGoSignUp, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Criar Conta") }
    }
}
