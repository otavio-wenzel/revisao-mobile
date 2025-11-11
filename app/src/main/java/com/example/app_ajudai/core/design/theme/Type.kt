package com.example.app_ajudai.core.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.app_ajudai.R // Importa o R do teu projeto

// Carrega as famílias de fontes da pasta res/font
val staatliches = FontFamily(
    Font(R.font.staatliches_regular, FontWeight.Normal)
)

val interTight = FontFamily(
    Font(R.font.inter_tight_regular, FontWeight.Normal),
    Font(R.font.inter_tight_bold, FontWeight.Bold)
    // Adiciona outros pesos (weights) se os tiveres baixado
)

// Define os estilos de tipografia da marca
val Typography = Typography(
    // Títulos (Ex: "Bem-vindo", "Feed de Pedidos")
    titleLarge = TextStyle(
        fontFamily = staatliches, // Fonte da marca para títulos
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp, // Tamanho maior
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp,
        color = AjudaiTextoEscuro // Cor de texto padrão
    ),

    // Títulos de Cards (Ex: "Preciso de ajuda com compras")
    titleMedium = TextStyle(
        fontFamily = interTight, // Fonte do corpo, mas a negrito
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Texto de Corpo (Ex: Descrições, labels)
    bodyLarge = TextStyle(
        fontFamily = interTight,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = interTight,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    // Texto de Botões
    labelLarge = TextStyle(
        fontFamily = interTight,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)