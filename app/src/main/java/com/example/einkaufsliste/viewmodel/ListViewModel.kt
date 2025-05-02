package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.einkaufsliste.model.usecase.GetShoppingListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListViewModel  (
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val getShoppingListUseCase: GetShoppingListUseCase = GetShoppingListUseCase()
    private val shoppingListId = savedStateHandle.get<String>("shoppingListId") ?: throw IllegalArgumentException("Keine Id für Shoppinglist bekommen")
    private val shoppingList = getShoppingListUseCase.getShoppingList(shoppingListId.toInt())

    private val listStateFlow = MutableStateFlow(ListViewModelState(shoppingList))
    val listViewState = listStateFlow.asStateFlow()

    fun updateSearchFieldState(isOpen: Boolean) {
        listStateFlow.update {
            it.copy(searchFieldOpen = isOpen)
        }
    }

    fun updateSearchFieldText(text: String) {
        listStateFlow.update {
            it.copy(searchFieldText = text)
        }
    }
}