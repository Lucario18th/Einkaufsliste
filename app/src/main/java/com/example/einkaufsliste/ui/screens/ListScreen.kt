package com.example.einkaufsliste.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.einkaufsliste.NavigationDestinations
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.ui.components.AddButton
import com.example.einkaufsliste.ui.components.SearchField
import com.example.einkaufsliste.viewmodel.ListViewModel
import com.example.einkaufsliste.viewmodel.ListViewModelState

@Composable
fun ListScreen(navController: NavController, viewModel: ListViewModel = viewModel()) {
    val state by viewModel.listViewState.collectAsState()
    Scaffold(
        topBar = { TopBar(state, viewModel, navigateBack = { navController.navigate(NavigationDestinations.ListsOverview) }) },
        floatingActionButton = {
            AddButton(onClick = { viewModel.changeAddEditItemDialogState(true, null) })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn {
                this.items(state.list.items) { item ->
                    if (state.searchFieldOpen && item.name.contains(state.searchFieldText)) {
                        ItemOnShoppingList(item, viewModel)
                    } else if (!state.searchFieldOpen) {
                        ItemOnShoppingList(item, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = Color.Blue,
        contentColor = Color.White,
        modifier = Modifier.size(75.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
private fun TopBar(state: ListViewModelState, viewModel: ListViewModel, navigateBack: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(top = 50.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable { navigateBack() }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )


            Text(
                text = state.list.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )


            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.clickable { viewModel.updateSearchFieldState(true) },
                tint = Color.White
            )
        }
        if (state.searchFieldOpen) {
            SearchField(
                searchFieldValue = state.searchFieldText,
                onValueChange = { viewModel.updateSearchFieldText(it) },
                onEmptySearchField = {
                    viewModel.updateSearchFieldText("")
                    viewModel.updateSearchFieldState(false)
                },
                onSearch = {}
            )
        }
    }
}

@Composable
private fun ItemOnShoppingList(shoppingItem: ShoppingItem, viewModel: ListViewModel) {
    Box(
        modifier = Modifier
            .padding(vertical = 15.dp, horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (shoppingItem.checked) Color.Green else Color.White)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
            .clickable { viewModel.toggleShoppingItemState(shoppingItem) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "${shoppingItem.number} ${shoppingItem.amountType.text}",
                color = Color.Gray,

                )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = shoppingItem.name,
                fontWeight = FontWeight.Bold,

                )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }

}