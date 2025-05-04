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
import com.example.einkaufsliste.ui.components.SearchField
import com.example.einkaufsliste.viewmodel.ListsOverviewViewModel
import com.example.einkaufsliste.viewmodel.ListsOverviewViewModelState

@Composable
fun ListsOverviewScreen(
    navController: NavController,
    viewModel: ListsOverviewViewModel = viewModel()
) {
    val state by viewModel.listOverviewViewState.collectAsState()
    Scaffold(
        topBar = { TopBar(state, viewModel) },
        floatingActionButton = {
            AddButton(onClick = { viewModel.changeAddRenameListDialogState(true) })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        if (state.showAddListSheet) {
            AddRenameListDialog(viewModel, state)
        }

        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            this.items(state.allLists) { list ->
                if (state.searchFieldOpen && list.name.contains(state.searchTextField)) {
                    ListListItem(shoppingList = list) {
                        navController.navigate(NavigationDestinations.List.name + "/$it")
                    }
                } else if (!state.searchFieldOpen) {
                    ListListItem(shoppingList = list) {
                        navController.navigate(NavigationDestinations.List.name + "/$it")
                    }
                }
            }
        }
    }
}

@Composable
private fun AddRenameListDialog(
    viewModel: ListsOverviewViewModel,
    state: ListsOverviewViewModelState,
) {
    Dialog(
        onDismissRequest = {
            viewModel.changeAddRenameListDialogState(false)
            viewModel.updateAddRenameListText("")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.shoppingListToRename == null) "Einkaufsliste hinzufügen" else "Einkaufsliste umbenennen",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            OutlinedTextField(
                value = state.addRenameListTextField,
                onValueChange = { viewModel.updateAddRenameListText(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                placeholder = { Text(text = if (state.shoppingListToRename == null) "Name" else "neuer Name") },
                singleLine = true,
                trailingIcon = {
                    if (state.addRenameListTextField != "") {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { viewModel.updateAddRenameListText("") })
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedTrailingIconColor = Color.Black,
                    unfocusedTrailingIconColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Blue,

                    )
            )
            Button(
                onClick = {
                    viewModel.updateAddRenameListText("")
                    viewModel.changeAddRenameListDialogState(false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(2.dp, Color.Blue, RoundedCornerShape(30.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                )
            ) {
                Text(text = "Abbrechen", color = Color.Blue)
            }
            Button(
                onClick = {
                    if (state.shoppingListToRename == null) viewModel.addShoppingList(state.addRenameListTextField)
                    else viewModel.updateListName(state.shoppingListToRename)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = if (state.shoppingListToRename == null) "Erstellen" else "Umbennenen", color = Color.White)
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
            SearchField(
                searchFieldValue = state.searchTextField,
                onValueChange = { viewModel.updateSearchListText(it) },
                onEmptySearchField = {
                    viewModel.updateSearchListText("")
                    viewModel.changeSearchbarState(false)
                }
            ) { }
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
