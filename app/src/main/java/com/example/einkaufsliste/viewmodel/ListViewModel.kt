package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListViewModel: ViewModel() {
    private val listStateFlow = MutableStateFlow(ListViewModelState())
    val listViewState = listStateFlow.asStateFlow()

    fun addShoppingList(name: String) {
        if (name == "") {
            listStateFlow.update { it.copy(addListTextFieldHasError = true) }
        } else {
            val shoppingLists: MutableList<ShoppingList> =
                listStateFlow.value.allLists.toMutableList()
            shoppingLists.add(ShoppingList(name = name))
            listStateFlow.update { it.copy(allLists = shoppingLists) }
            changeAddListDialogState(false)
        }
    }

    fun updateSearchListText(text: String) {
        listStateFlow.update { it.copy(searchTextField = text) }
    }

    fun changeSearchbarState(state: Boolean) {
        listStateFlow.update { it.copy(searchFieldOpen = state) }
    }

    fun changeAddListDialogState(state: Boolean) {
        listStateFlow.update { it.copy(showAddListSheet = state) }
    }

    fun updateAddListText(text: String) {
        listStateFlow.update { it.copy(addListTextField = text) }
    }
}