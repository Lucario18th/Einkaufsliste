package com.example.einkaufsliste.ui.screens

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.einkaufsliste.viewmodel.ListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.einkaufsliste.viewmodel.ListViewModelState
import com.example.einkaufsliste.viewmodel.ShoppingList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(viewModel: ListViewModel = viewModel()) {
    val state by viewModel.listViewState.collectAsState()
    Scaffold(
        topBar = { TopBar(state, viewModel) },
        floatingActionButton = {
            AddButton(onClick = { viewModel.changeAddListSheetState(true) })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.LightGray
    ) { paddingValues ->
        if (state.showAddListSheet) {
            Dialog(
                onDismissRequest = { viewModel.changeAddListSheetState(false) },
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
                        value = state.addListFieldText,
                        onValueChange = { viewModel.updateAddListText(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        placeholder = { Text(text = "Name") },
                        singleLine = true,
                        label = { Text(text = "Name der Liste") },
                        leadingIcon = {
                            if (state.addListFieldText != "") {
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
                        )
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                    Button(
                        onClick = { viewModel.addShoppingList(state.addListFieldText) },
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

        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            this.items(state.allLists) { list ->
                ListListItem(shoppingList = list)
            }
        }
    }
}

@Composable
private fun AddButton(onClick:() -> Unit) {
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
private fun TopBar(state: ListViewModelState, viewModel: ListViewModel) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(top = 50.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)
        ) {
            Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = null)

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(text = "Einkaufslisten", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.clickable { viewModel.changeSearchbarState(true) }
            )
        }
        if (state.searchFieldOpen) {
            TextField(
                value = state.searchFieldText,
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
private fun ListListItem(shoppingList: ShoppingList) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = Color.White)
        ) {
            Text(text = shoppingList.name)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(text = "${shoppingList.completion}/${shoppingList.shoppingList.size}")
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black
        )
    }
}