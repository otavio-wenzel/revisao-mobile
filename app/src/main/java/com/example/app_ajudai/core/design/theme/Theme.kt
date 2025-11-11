package com.example.app_ajudai.core.design.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define a paleta de cores claras da marca
private val LightColorScheme = lightColorScheme(
    primary = AjudaiLaranja, // Cor principal (botões, ícones ativos)
    secondary = AjudaiMalva, // Cor secundária
    tertiary = AjudaiOliva, // Cor terciária
    background = AjudaiFundoClaro, // Cor de fundo da app
    surface = AjudaiBranco, // Cor de fundo de "superfícies" (cards, app bars)

    onPrimary = AjudaiBranco, // Cor do texto em cima do Laranja (ex: botão)
    onSecondary = AjudaiBranco,
    onTertiary = AjudaiBranco,
    onBackground = AjudaiTextoEscuro, // Cor do texto em cima do fundo
    onSurface = AjudaiTextoEscuro // Cor do texto em cima de cards
)

// (Vamos manter a paleta escura como padrão por agora, para focar no tema claro)
private val DarkColorScheme = darkColorScheme(
    primary = AjudaiLaranja,
    secondary = AjudaiMalva,
    tertiary = AjudaiOliva
)

@Composable
fun AppajudaiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desativamos o 'dynamicColor' do Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme // Usar sempre o nosso tema claro
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb() // Cor da barra de status
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, // Aplica as NOSSAS cores
        typography = Typography, // Aplica as NOSSAS fontes
        content = content
    )
}