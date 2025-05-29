package com.example.einkaufsliste.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.einkaufsliste.NavigationDestinations
import com.example.einkaufsliste.R
import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.ui.components.SearchField
import com.example.einkaufsliste.viewmodel.ListViewModel
import com.example.einkaufsliste.viewmodel.ListViewModelState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, viewModel: ListViewModel = viewModel()) {
    val state by viewModel.listViewState.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        topBar = {
            TopBar(
                state,
                viewModel,
                navigateBack = { navController.navigate(NavigationDestinations.ListsOverview.name) })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        sheetContent = { AddItemBottomSheet(viewModel, state, sheetState) },
        scaffoldState = sheetState,
        sheetPeekHeight = 250.dp,
        sheetContainerColor = Color.DarkGray
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (state.shoppingItemToDelete != null) {
                DeleteListDialog(viewModel, state)
            }
            if (state.shoppingItemToEdit != null) {
                EditItemDialog(viewModel, state)
            }
            LazyColumn {
                if (state.searchFieldOpen) {
                    this.items(state.list.items) { item ->
                        if (item.name.contains(state.searchFieldText))
                            ItemOnShoppingList(item, viewModel, state)
                    }
                } else {
                    this.items(state.list.items) { item ->
                        if (!item.checked) ItemOnShoppingList(item, viewModel, state)
                    }
                    if (state.list.items.isNotEmpty()) {
                        this.item {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                thickness = 2.dp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                this.items(state.list.items) { item ->
                    if (item.checked) ItemOnShoppingList(item, viewModel, state)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemBottomSheet(
    viewModel: ListViewModel,
    state: ListViewModelState,
    sheetState: BottomSheetScaffoldState,
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 30.dp, end = 30.dp),
    ) {
        Text(
            text = "Eintrag hinzufügen",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
        if (sheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        scope.launch {
                            sheetState.bottomSheetState.partialExpand()
                            viewModel.makeValuesDefault()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }

    if (sheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = state.addAmountText,
                onValueChange = { viewModel.updateAddAmountTextField(it) },
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

            Column(modifier = Modifier.fillMaxWidth(1.0f)) {
                OutlinedTextField(
                    value = state.addAmountType.text,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateAddAmountMenuState(true) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        disabledTextColor = Color.Black,
                        disabledTrailingIconColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray,
                        disabledIndicatorColor = if (state.editAmountMenuOpen) Color.Blue else Color.Gray
                    ),
                    enabled = false,
                    trailingIcon = {
                        if (state.editAmountMenuOpen) Icon(
                            Icons.Filled.KeyboardArrowDown,
                            null
                        ) else Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                    }
                )
                DropdownMenu(
                    expanded = state.addAmountMenuOpen,
                    onDismissRequest = { viewModel.updateAddAmountMenuState(false) }
                ) {
                    Amount.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.text) },
                            onClick = { viewModel.updateAddAmountType(it) }
                        )
                    }
                }
            }
        }
    }

    OutlinedTextField(
        value = state.addShoppingItemText,
        onValueChange = { viewModel.updateAddTextField(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        scope.launch { sheetState.bottomSheetState.expand() }
                    }
                }
            },
        placeholder = { Text(text = "Name") },
        singleLine = true,
        trailingIcon = {
            if (state.addShoppingItemText != "") {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable { viewModel.updateAddTextField("") })
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
        ),
    )

    if (sheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
        Button(
            onClick = {
                viewModel.createShoppingItem(
                    name = state.addShoppingItemText,
                    amountType = state.addAmountType,
                    number = state.addAmountText
                )
                scope.launch { sheetState.bottomSheetState.partialExpand() }
                focusManager.clearFocus()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.Blue),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Erstellen", color = Color.White)
        }
    }

    Spacer(modifier = Modifier.fillMaxHeight(0.85f))

}

@Composable
private fun EditItemDialog(
    viewModel: ListViewModel,
    state: ListViewModelState,
) {
    val focusManager = LocalFocusManager.current
    Dialog(
        onDismissRequest = {
            viewModel.changeEditItemDialogState(null)
            viewModel.updateEditTextField("")
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
                    text = "Eintrag bearbeiten",
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
                    value = state.editAmountText,
                    onValueChange = { viewModel.updateEditAmountTextField(it) },
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

                Column(modifier = Modifier.fillMaxWidth(1.0f)) {
                    OutlinedTextField(
                        value = state.editAmountType.text,
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.updateEditAmountMenuState(true)
                                focusManager.clearFocus()
                            },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = Color.White,
                            disabledTextColor = Color.Black,
                            disabledTrailingIconColor = Color.Black,
                            disabledPlaceholderColor = Color.Gray,
                            disabledIndicatorColor = if (state.editAmountMenuOpen) Color.Blue else Color.Gray
                        ),
                        enabled = false,
                        trailingIcon = {
                            if (state.editAmountMenuOpen) Icon(
                                Icons.Filled.KeyboardArrowDown,
                                null
                            ) else Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                        }
                    )
                    DropdownMenu(
                        expanded = state.editAmountMenuOpen,
                        onDismissRequest = { viewModel.updateEditAmountMenuState(false) }
                    ) {
                        Amount.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(it.text) },
                                onClick = { viewModel.updateEditAmountType(it) }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.editShoppingItemText,
                onValueChange = { viewModel.updateEditTextField(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                placeholder = { Text(text = "neuer Name") },
                singleLine = true,
                trailingIcon = {
                    if (state.editShoppingItemText != "") {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { viewModel.updateEditTextField("") })
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
                    viewModel.updateEditTextField("")
                    viewModel.changeEditItemDialogState(null)
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
                    viewModel.updateShoppingItem(
                        name = state.editShoppingItemText,
                        amountType = state.editAmountType,
                        number = state.editAmountText

                    )
                    viewModel.changeEditItemDialogState(null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.Blue),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Fertig", color = Color.White)
            }
        }
    }
}

@Composable
private fun TopBar(
    state: ListViewModelState,
    viewModel: ListViewModel,
    navigateBack: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(top = 50.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemOnShoppingList(
    shoppingItem: ShoppingItem,
    viewModel: ListViewModel,
    state: ListViewModelState
) {
    Box(
        modifier = Modifier
            .padding(vertical = 15.dp, horizontal = 20.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { viewModel.toggleShoppingItemState(shoppingItem) },
                onLongClick = { viewModel.updateItemMenuDropdownForId(shoppingItem.id) }
            ),
    ) {
        Box(
            modifier = Modifier
                .padding()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "${shoppingItem.number} ${shoppingItem.amountType.text}",
                    color = Color.Gray,
                    textDecoration = if (shoppingItem.checked) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = shoppingItem.name,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (shoppingItem.checked) TextDecoration.LineThrough else null
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

        DropdownMenu(
            expanded = state.itemMenuDropdownOpenForId == shoppingItem.id,
            onDismissRequest = { viewModel.updateItemMenuDropdownForId(null) },
            offset = DpOffset(20.dp, 0.dp),
        ) {
            DropdownMenuItem(
                text = { Text("Bearbeiten") },
                onClick = {
                    viewModel.changeEditItemDialogState(shoppingItem)
                    viewModel.updateItemMenuDropdownForId(null)
                },
                leadingIcon = { Icon(imageVector = Icons.Default.Edit, null) }
            )
            DropdownMenuItem(
                text = { Text("Löschen") },
                onClick = {
                    viewModel.updateDeleteItemDialogState(shoppingItem)
                    viewModel.updateItemMenuDropdownForId(null)
                },
                leadingIcon = { Icon(imageVector = Icons.Default.Delete, null) }
            )
        }
    }
}

@Composable
private fun DeleteListDialog(
    viewModel: ListViewModel,
    state: ListViewModelState
) {
    Dialog(
        onDismissRequest = { viewModel.updateDeleteItemDialogState(null) }
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
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.warning_icon),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                text = "Willst du den Eintrag \"${state.shoppingItemToDelete?.name?: ""}\" wirklich löschen?",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { viewModel.updateDeleteItemDialogState(null) },
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
                    if (state.shoppingItemToDelete != null) {
                        viewModel.deleteShoppingItem(state.shoppingItemToDelete.id)
                    }
                    viewModel.updateDeleteItemDialogState(null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.Blue),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(
                    text = "Löschen",
                    color = Color.White
                )
            }
        }
    }
}