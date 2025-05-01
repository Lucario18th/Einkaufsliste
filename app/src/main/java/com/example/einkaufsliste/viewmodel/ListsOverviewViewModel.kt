package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.usecase.CreateShoppingListUseCase
import com.example.einkaufsliste.model.usecase.GetRandomKeyValueUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListsOverviewViewModel(): ViewModel() {
    val createShoppingList = CreateShoppingListUseCase()
    val getRandomKeyValue = GetRandomKeyValueUseCase()

    private val listStateFlow = MutableStateFlow(ListsOverviewViewModelState())
    val listViewState = listStateFlow.asStateFlow()

    fun addShoppingList(name: String) {
        if (name == "") {
            listStateFlow.update { it.copy(addListTextFieldHasError = true) }
        } else {
            val shoppingLists: MutableList<ShoppingList> =
                listStateFlow.value.allLists.toMutableList()
            val nextId = getRandomKeyValue()
            val newShoppingList = ShoppingList(nextId, name)
            shoppingLists.add(newShoppingList)
            listStateFlow.update { it.copy(allLists = shoppingLists) }
            changeAddListDialogState(false)
            viewModelScope.launch {
                createShoppingList(newShoppingList)
            }
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