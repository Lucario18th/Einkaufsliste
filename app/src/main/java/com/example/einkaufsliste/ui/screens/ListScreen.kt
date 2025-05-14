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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.ui.components.AddButton
import com.example.einkaufsliste.ui.components.SearchField
import com.example.einkaufsliste.viewmodel.ListViewModel
import com.example.einkaufsliste.viewmodel.ListViewModelState

@Composable
fun ListScreen(navController: NavController, viewModel: ListViewModel = viewModel()) {
    val state by viewModel.listViewState.collectAsState()
    Scaffold(
        topBar = { TopBar(state, viewModel, navigateBack = { navController.navigate(NavigationDestinations.ListsOverview.name) }) },
        floatingActionButton = {
            AddButton(onClick = { viewModel.changeAddEditItemDialogState(true, null) })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (state.addEditItemDialogOpen) {
                AddEditItemDialog(viewModel, state)
            }
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
private fun AddEditItemDialog(
    viewModel: ListViewModel,
    state: ListViewModelState,
) {
    Dialog(
        onDismissRequest = {
            viewModel.changeAddEditItemDialogState(false)
            viewModel.updateAddEditTextField("")
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
                    text = if (state.shoppingItemToEdit == null) "Eintrag hinzufügen" else "Eintrag bearbeiten",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                OutlinedTextField(
                    value = state.amountText,
                    onValueChange = { viewModel.updateAmountTextField(it) },
                    modifier = Modifier
                        .fillMaxWidth(0.25f),
                    placeholder = { Text(text = "1") },
                    singleLine = true,
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
                        unfocusedIndicatorColor = Color.Gray,
                        ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = state.chosenAmountType.text,
                    onValueChange = {  },
                    modifier = Modifier
                        .fillMaxWidth(1.0f)
                        .clickable { viewModel.updateAmountMenuState(true) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        disabledTextColor = Color.Black,
                        disabledTrailingIconColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray,
                        disabledIndicatorColor = if (state.amountMenuOpen) Color.Blue else Color.Gray
                    ),
                    enabled = false,
                    trailingIcon = {
                        if (state.amountMenuOpen) Icon(Icons.Filled.KeyboardArrowDown, null) else Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                    }
                )
                DropdownMenu(
                    expanded = state.amountMenuOpen,
                    onDismissRequest = { viewModel.updateAmountMenuState(false) }
                ) {
                    Amount.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.text) },
                            onClick = { viewModel.updateAmountType(it) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.addEditShoppingItemText,
                onValueChange = { viewModel.updateAddEditTextField(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                placeholder = { Text(text = if (state.shoppingItemToEdit == null) "Name" else "neuer Name") },
                singleLine = true,
                trailingIcon = {
                    if (state.addEditShoppingItemText != "") {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { viewModel.updateAddEditTextField("") })
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
                    unfocusedIndicatorColor = Color.Gray,
                    )
            )
            Button(
                onClick = {
                    viewModel.updateAddEditTextField("")
                    viewModel.changeAddEditItemDialogState(false)
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
                    viewModel.createShoppingItem(
                        name = state.addEditShoppingItemText,
                        amountType = state.chosenAmountType,
                        number = state.amountText.toInt()
                    )
                    viewModel.changeAddEditItemDialogState(false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.Blue),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = if (state.shoppingItemToEdit == null) "Erstellen" else "Umbennenen", color = Color.White)
            }
        }
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