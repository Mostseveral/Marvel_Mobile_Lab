package com.example.androidapp.screens

import android.util.Log
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil.compose.rememberAsyncImagePainter
import com.example.androidapp.R
import com.example.androidapp.ui.theme.Typography
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidapp.database.AppDatabase
import com.example.androidapp.database.toEntity
import com.example.androidapp.database.toUI

@Composable
fun HeroDetailScreen(
    navController: NavController,
    heroId: Int,
    heroRepository: HeroRepository = HeroRepository(AppDatabase.getDatabase(context = LocalContext.current))
) {
    var hero by remember { mutableStateOf<Hero?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(heroId) {
        try {
            val heroFromDb = heroRepository.getHeroById(heroId)
            if (heroFromDb != null) {
                Log.d("HeroDetailScreen", "etot hero from database: ${heroFromDb.name}")
                hero = heroFromDb
            } else {
                val heroFromNetwork = MarvelApiClient.getHeroes(1, heroId - 1).firstOrNull()
                if (heroFromNetwork != null) {
                    Log.d("HeroDetailScreen", "etot hero from API: ${heroFromNetwork.name}")
                    heroRepository.insertHero(heroFromNetwork.toEntity())
                    hero = heroFromNetwork.toUI()
                } else {
                    errorMessage = "Cant load Hero."
                    Log.e("HeroDetailScreen", "Ne udalos naiti geroya $heroId")
                }
            }
        } catch (e: Exception) {
            Log.e("HeroDetailScreen", "Error loading hero: ${e.message}", e)
            errorMessage = "Loading error."
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Text(
                    text = "Loading",
                    modifier = Modifier.align(Alignment.Center),
                    style = Typography.bodyLarge,
                    color = Color.White
                )
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    modifier = Modifier.align(Alignment.Center),
                    style = Typography.bodyLarge,
                    color = Color.Red
                )
            }

            hero != null -> {
                // Основное отображение героя
                val secureImage = hero!!.image.replace("http://", "https://")
                Image(
                    painter = rememberAsyncImagePainter(secureImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = hero!!.name,
                        style = Typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = hero!!.description.ifEmpty { "Marvel Hero Without Description." },
                        style = Typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Назад",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}

