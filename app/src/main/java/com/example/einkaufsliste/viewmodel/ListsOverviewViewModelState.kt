package com.example.einkaufsliste.viewmodel

import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.model.models.ShoppingList

data class ListsOverviewViewModelState(
    val allLists: List<ShoppingList> = emptyList(),
    val openedList: ShoppingItem? = null,
    val searchFieldOpen: Boolean = false,
    val searchTextField: String = "",
    val showAddListSheet: Boolean = false,
    val addRenameListTextField: String = "",
    val addListTextFieldHasError: Boolean = false,
    val shoppingListToRename: ShoppingList? = null,
    val listMenuDropdownOpen: Boolean = false,
    val shoppingListToDelete: ShoppingList? = null
)
