package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.einkaufsliste.model.models.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shoppingListId = savedStateHandle.get<String>("shoppingListId")

    private val listStateFlow = MutableStateFlow(ListViewModelState(ShoppingList(1, "")))
    val listViewState = listStateFlow.asStateFlow()
}