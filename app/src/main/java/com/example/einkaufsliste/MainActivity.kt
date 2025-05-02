package com.example.einkaufsliste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.einkaufsliste.ui.screens.ListScreen
import com.example.einkaufsliste.ui.screens.ListsOverviewScreen
import com.example.einkaufsliste.ui.theme.EinkaufslisteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EinkaufslisteTheme {
                Navigation()
            }
        }
    }
}

enum class NavigationDestinations() {
    ListsOverview,
    List
}


@Composable
private fun Navigation() {
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination = NavigationDestinations.ListsOverview.name
    ) {
        composable(route = NavigationDestinations.ListsOverview.name) {
            ListsOverviewScreen(navController)
        }

        composable(route = "${NavigationDestinations.List.name}/{shoppingListId}") {
            val shoppingListId = it.arguments?.getString("shoppingListId")!!
            ListScreen(navController)
        }
    }
}