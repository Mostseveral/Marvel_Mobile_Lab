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
fun HeroDetailScreen(
    navController: NavController,
    name: String,
    image: String,
    description: String
) {
    // Используем rememberAsyncImagePainter для загрузки изображения по URL
    val painter = rememberAsyncImagePainter(
        model = image,
        placeholder = painterResource(id = R.drawable.placeholder), // Плейсхолдер
        error = painterResource(id = R.drawable.error_image) // Изображение ошибки
    )

    val state = painter.state

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is coil.compose.AsyncImagePainter.State.Loading -> {
                // Показываем placeholder, пока изображение загружается
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Loading...",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            is coil.compose.AsyncImagePainter.State.Success -> {
                // Показываем загруженное изображение
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            is coil.compose.AsyncImagePainter.State.Error -> {
                // Показываем изображение ошибки, если загрузка не удалась
                Image(
                    painter = painterResource(id = R.drawable.error_image),
                    contentDescription = "Error",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                // Запасной вариант (должно быть покрыто выше)
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Default",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Кнопка "Назад"
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Кнопка назад",
                modifier = Modifier.size(50.dp)
            )
        }

        // Текст с описанием героя
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .align(Alignment.BottomCenter)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = name,
                    style = Typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = description.ifBlank { "Cool Marvel Hero." },
                    style = Typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 25.dp)
                )
            }
        }
    }
}
