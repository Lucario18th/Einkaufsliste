package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.usecase.GetRandomKeyValueUseCase
import com.example.einkaufsliste.model.usecase.list.CreateShoppingListUseCase
import com.example.einkaufsliste.model.usecase.list.DeleteShoppingListUseCase
import com.example.einkaufsliste.model.usecase.list.GetShoppingListUseCase
import com.example.einkaufsliste.model.usecase.list.UpdateShoppingListNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListsOverviewViewModel(): ViewModel() {
    private val createShoppingList = CreateShoppingListUseCase()
    private val getRandomKeyValue = GetRandomKeyValueUseCase()
    private val getShoppingListUseCase = GetShoppingListUseCase()
    private val renameShoppingList = UpdateShoppingListNameUseCase()
    private val deleteShoppingListUseCase = DeleteShoppingListUseCase()

    private val listOverviewStateFlow = MutableStateFlow(ListsOverviewViewModelState())
    val listOverviewViewState = listOverviewStateFlow.asStateFlow()

    init {
        loadAllLists()
    }

    private fun loadAllLists() {
        val allLists = getShoppingListUseCase.getAllShoppingLists()
        listOverviewStateFlow.update { it.copy(allLists = allLists) }
    }

    fun addShoppingList(name: String) {
        if (name == "") {
            listOverviewStateFlow.update { it.copy(addListTextFieldHasError = true) }
        } else {
            val shoppingLists: MutableList<ShoppingList> =
                listOverviewStateFlow.value.allLists.toMutableList()
            val nextId = getRandomKeyValue()
            val newShoppingList = ShoppingList(nextId, name)
            shoppingLists.add(newShoppingList)
            listOverviewStateFlow.update { it.copy(allLists = shoppingLists, addRenameListTextField = "") }
            updateAddRenameListDialogState(false)
            viewModelScope.launch {
                createShoppingList(newShoppingList)
            }
        }
    }

    fun updateSearchListText(text: String) {
        listOverviewStateFlow.update { it.copy(searchTextField = text) }
    }

    fun changeSearchbarState(state: Boolean) {
        listOverviewStateFlow.update { it.copy(searchFieldOpen = state) }
    }

    fun updateAddRenameListDialogState(state: Boolean, shoppingListToRename: ShoppingList? = null) {
        listOverviewStateFlow.update { it.copy(showAddListSheet = state, shoppingListToRename = shoppingListToRename, addRenameListTextField = shoppingListToRename?.name?:  "") }
    }

    fun updateAddRenameListText(text: String) {
        listOverviewStateFlow.update { it.copy(addRenameListTextField = text) }
    }

    fun updateListName(shoppingList: ShoppingList) {
        val newName = listOverviewViewState.value.addRenameListTextField
        if (shoppingList.name != newName) renameShoppingList(shoppingList.id, newName)
        listOverviewStateFlow.update { it.copy(addRenameListTextField = "", showAddListSheet = false) }
        loadAllLists()
    }

    fun updateListMenuDropdownForId(id: Int?) {
        listOverviewStateFlow.update { it.copy(listMenuDropdownOpenForId = id) }
    }

    fun updateDeleteListDialogState(shoppingList: ShoppingList?) {
        listOverviewStateFlow.update { it.copy(shoppingListToDelete = shoppingList) }
    }

    fun deleteShoppingList(shoppingList: ShoppingList) {
        deleteShoppingListUseCase(shoppingList.id)
        loadAllLists()
    }
}