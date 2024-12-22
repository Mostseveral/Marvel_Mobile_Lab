package com.example.androidapp.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.androidapp.R
import com.example.androidapp.ui.theme.Marvelgrey
import com.example.androidapp.ui.theme.Typography
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Date

data class MarvelResponse(
    val data: MarvelData
)

data class MarvelData(
    val results: List<MarvelHero>
)

data class MarvelHero(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: MarvelThumbnail
)

data class MarvelThumbnail(
    val path: String,
    val extension: String
) {

    val fullPath: String
        get() = "$path.$extension"
}

interface MarvelApiService {
    @GET("v1/public/characters")
    suspend fun getHeroes(
        @Query("ts") ts: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): MarvelResponse
}

object MarvelApiClient {
    private const val BASE_URL = "https://gateway.marvel.com/"
    private const val PUBLIC_KEY = "06f972f918b3a1ea16ed02be244bd1dd"
    private const val PRIVATE_KEY = "5850e28453ca970a02f080788e25c2aad32dedf1"

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: MarvelApiService by lazy {
        retrofit.create(MarvelApiService::class.java)
    }

    suspend fun getHeroes(limit: Int, offset: Int): List<MarvelHero> {
        val ts = Date().time.toString()
        val hash = md5("$ts$PRIVATE_KEY$PUBLIC_KEY")
        val response = service.getHeroes(ts, PUBLIC_KEY, hash, limit, offset)
        return response.data.results
    }
}

class HeroRepository {
    suspend fun getAllHeroes(offset: Int, limit: Int = 10): List<Hero> {
        val allHeroes = mutableListOf<Hero>()
        try {
            val heroesBatch = MarvelApiClient.getHeroes(limit, offset)
            Log.d("HeroRepository", "Loaded ${heroesBatch.size} heroes at offset $offset")
            if (heroesBatch.isNotEmpty()) {
                allHeroes.addAll(heroesBatch.map {
                    Hero(
                        id = it.id,
                        name = it.name,
                        image = it.thumbnail.fullPath,
                        description = it.description
                    )
                })
            }
        } catch (e: Exception) {
            Log.e("HeroRepository", "Error loading heroes: ${e.message}", e)
        }
        Log.d("HeroRepository", "Total loaded heroes: ${allHeroes.size}")
        return allHeroes
    }
}

@Composable
fun HeroListScreen(navController: NavController, heroRepository: HeroRepository = HeroRepository()) {
    var heroes by remember { mutableStateOf(emptyList<Hero>()) }
    var isLoading by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    suspend fun loadMoreHeroes() {
        if (isLoading) return
        isLoading = true
        try {
            val newHeroes = heroRepository.getAllHeroes(offset)
            heroes = heroes + newHeroes
            offset += newHeroes.size
            errorMessage = null // Сбрасываем сообщение об ошибке, если всё успешно
        } catch (e: Exception) {
            Log.e("HeroListScreen", "Error loading heroes: ${e.message}", e)
            errorMessage = "Failed to load heroes. Please check your internet connection."
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadMoreHeroes()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { visibleItem ->
                if (visibleItem != null && visibleItem.index == heroes.size - 1 && !isLoading) {
                    loadMoreHeroes()
                }
            }
    }

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

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = Typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            LazyRow(
                state = listState,
                flingBehavior = snapBehavior,
                content = {
                    items(heroes.size) { index ->
                        val hero = heroes[index]
                        HeroListItem(hero = hero, onClick = {
                            navController.navigate(
                                "hero_detail/${Uri.encode(hero.name)}/${Uri.encode(hero.image)}/${Uri.encode(hero.description)}"
                            )
                        })
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
            .width(400.dp)
            .height(600.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        val secureImageUrl = hero.image.replace("http://", "https://")
        val painter = rememberAsyncImagePainter(
            secureImageUrl,
            error = painterResource(id = R.drawable.placeholder)
        )
        Log.d("IMAGE", hero.image)
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = hero.name,
            style = Typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .padding(4.dp)
        )
    }
}

data class Hero(val name: String, val image: String, val description: String, val id: Int)