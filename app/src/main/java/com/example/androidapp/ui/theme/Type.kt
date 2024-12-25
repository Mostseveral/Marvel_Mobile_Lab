package com.example.androidapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.androidapp.R

val HeroNameFont = FontFamily(Font(R.font.desc))
val MarvelFont = FontFamily(Font(R.font.marvelfont))
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MarvelFont,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = MarvelFont,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp,
        lineHeight = 53.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = HeroNameFont,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 45.sp,
        letterSpacing = 2.sp
    )

)