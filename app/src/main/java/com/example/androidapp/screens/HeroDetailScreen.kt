package com.example.androidapp.screens

import android.util.Log
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import coil.compose.rememberAsyncImagePainter
import com.example.androidapp.R
import com.example.androidapp.ui.theme.Typography
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun HeroDetailScreen(navController: NavController, name: String, image: String, description: String) {
    // Замена http на https
    val secureImage = image.replace("http://", "https://")
    Log.d("HeroDetailScreen", "Image URL: $secureImage")

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Фоновая картинка героя
        Image(
            painter = rememberAsyncImagePainter(secureImage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        @Composable
        fun BackButton(navController: NavController) {
            IconButton(onClick = { navController.popBackStack() }) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "кнопка назад",
                    modifier = Modifier.size(50.dp),
                )
            }
        }
        BackButton(navController = navController)

        // Имя и описание героя внизу по центру
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .align(Alignment.BottomCenter) // Контент внизу по центру
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomCenter) // Выравнивание снизу по центру
            ) {
                // Имя героя
                Text(
                    text = name,
                    style = Typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 10.dp) // Отступ от текста
                )

                // Описание героя
                Text(
                    text = description,
                    style = Typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 25.dp)
                )
            }
        }
    }
}
