package com.example.androidapp

import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import com.example.androidapp.screens.HeroDetailScreen
import com.example.androidapp.screens.HeroListScreen
import androidx.navigation.navArgument


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            HeroApp(navController)
        }
    }
}

@Composable
fun HeroApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "hero_list") {
        composable("hero_list") {
            HeroListScreen(navController = navController)
        }
        composable(
            route = "hero_detail/{heroId}",
            arguments = listOf(navArgument("heroId") { type = NavType.IntType })
        ) { backStackEntry ->
            val heroId = backStackEntry.arguments?.getInt("heroId") ?: 0
            HeroDetailScreen(
                navController = navController,
                heroId = heroId
            )
        }
    }
}