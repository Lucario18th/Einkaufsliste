package com.example.einkaufsliste.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.einkaufsliste.NavigationDestinations
import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.models.completion
import com.example.einkaufsliste.viewmodel.ListsOverviewViewModel
import com.example.einkaufsliste.viewmodel.ListsOverviewViewModelState

@Composable
fun ListsOverviewScreen(navController: NavController, viewModel: ListsOverviewViewModel = viewModel()) {
    val state by viewModel.listOverviewViewState.collectAsState()
    Scaffold(
        topBar = { TopBar(state, viewModel) },
        floatingActionButton = {
            AddButton(onClick = { viewModel.changeAddListDialogState(true) })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        if (state.showAddListSheet) {
            AddListDialog(viewModel, state)
        }

        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            this.items(state.allLists) { list ->
                ListListItem(shoppingList = list) {
                    navController.navigate(NavigationDestinations.List.name + "/$it")
                }
            }
        }
    }
}

@Composable
private fun AddListDialog(
    viewModel: ListsOverviewViewModel,
    state: ListsOverviewViewModelState
) {
    Dialog(
        onDismissRequest = { viewModel.changeAddListDialogState(false) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Einkaufsliste hinzufügen",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            OutlinedTextField(
                value = state.addListTextField,
                onValueChange = { viewModel.updateAddListText(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                placeholder = { Text(text = "Name") },
                singleLine = true,
                label = { Text(text = "Name der Liste") },
                leadingIcon = {
                    if (state.addListTextField != "") {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { viewModel.updateAddListText("") })
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLeadingIconColor = Color.Black,
                    unfocusedLeadingIconColor = Color.Black,
                    focusedPlaceholderColor = Color.DarkGray,
                    unfocusedPlaceholderColor = Color.DarkGray
                )
            )
            Spacer(modifier = Modifier.fillMaxWidth(1f))
            Button(
                onClick = { viewModel.addShoppingList(state.addListTextField) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Erstellen", color = Color.White)
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
private fun TopBar(state: ListsOverviewViewModelState, viewModel: ListsOverviewViewModel) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(top = 50.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(
                text = "Einkaufslisten",
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
                modifier = Modifier.clickable { viewModel.changeSearchbarState(true) },
                tint = Color.White
            )
        }
        if (state.searchFieldOpen) {
            TextField(
                value = state.searchTextField,
                onValueChange = { viewModel.updateSearchListText(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.changeSearchbarState(false)
                            viewModel.updateSearchListText("")
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun ListListItem(shoppingList: ShoppingList, navigateToShoppingList: (id: Int) -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 15.dp, horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
            .clickable { navigateToShoppingList(shoppingList.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = shoppingList.name)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(text = "${shoppingList.completion()}/${shoppingList.items.size}")
        }
    }

}
