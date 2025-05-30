package com.example.einkaufsliste.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.einkaufsliste.NavigationDestinations

@Composable
fun CustomAppScaffold(
    tobBar: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { tobBar() },
        floatingActionButton = { floatingActionButton() },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,

    ) { paddingValues ->
        content(paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomNavigationBar(
    currentDestination: NavDestination,
    navController: NavController
) {
    NavigationBar(
        containerColor = Color.Blue,
        contentColor = Color.White,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        NavigationDestinations.entries.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = currentDestination.id == index,
                onClick = {
                    navController.navigate(destination)
                },
                icon = {
                    when(destination) {
                        NavigationDestinations.ListsOverview -> Icon(imageVector = Icons.Default.Home, null)
                        NavigationDestinations.List -> Icon(imageVector = Icons.AutoMirrored.Filled.List, null)
                    }
                }
            )
        }
    }
}