package com.example.androidapp.screens

import android.net.Uri
import android.util.Log
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidapp.ui.theme.Typography
import androidx.compose.ui.res.painterResource
import com.example.androidapp.R
import com.example.androidapp.ui.theme.*

@Composable
fun HeroListScreen(navController: NavController) {
    val heroes = listOf(
        Hero("Spider-Man", "https://www.strangearts.ru/sites/default/files/heroes/catalog/images/spider-man.jpg", "Friendly neighbor Spider-Man"),
        Hero("Iron Man", "https://www.strangearts.ru/sites/default/files/heroes/catalog/images/im_123.jpg", "Billionaire, genius, philanthropist"),
        Hero("The Hulk", "https://www.strangearts.ru/sites/default/files/heroes/catalog/images/cosmic_hulk.jpg", "Green rage machine called the Hulk."),
        Hero("Deadpool", "https://www.strangearts.ru/sites/default/files/heroes/catalog/images/2703120-dpool2012005_var.jpg", "Pool")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Marvelgrey),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        )

        Text(
            text = "Choose your hero",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = Typography.titleLarge

        )

        val listState = rememberLazyListState()
        val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                state = listState,
                flingBehavior = snapBehavior,
                content = {
                    items(heroes.size) { index -> // Здесь мы используем index
                        val hero = heroes[index] // Получаем героя по индексу
                        HeroListItem(hero = hero) {
                            Log.d("NavGraph", "Navigating to hero_detail: ${hero.name}, ${hero.image}, ${hero.description}")
                            navController.navigate("hero_detail/${Uri.encode(hero.name)}/${Uri.encode(hero.image)}/${Uri.encode(hero.description)}")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HeroListItem(hero: Hero, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(370.dp)
            .height(600.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(hero.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = hero.name,
            style = Typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .padding(4.dp)
        )
    }
}

data class Hero(val name: String, val image: String, val description: String)