package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.einkaufsliste.model.usecase.GetShoppingListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel(
    savedStateHandle: SavedStateHandle,
    getShoppingListUseCase: GetShoppingListUseCase = GetShoppingListUseCase()
) : ViewModel() {

    private val shoppingListId = savedStateHandle.get<String>("shoppingListId") ?: throw IllegalArgumentException("Keine Id für Shoppinglist bekommen")

    private val listStateFlow = MutableStateFlow(ListViewModelState(getShoppingListUseCase.getShoppingList(shoppingListId.toInt())))
    val listViewState = listStateFlow.asStateFlow()
}