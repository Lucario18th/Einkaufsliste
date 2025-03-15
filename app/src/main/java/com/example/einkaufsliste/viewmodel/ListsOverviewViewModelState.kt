package com.example.einkaufsliste.viewmodel

import com.example.einkaufsliste.model.models.ShoppingList

data class ListsOverviewViewModelState(
    val allLists: List<ShoppingList> = emptyList(),
    val openedList: ListItem? = null,
    val searchFieldOpen: Boolean = false,
    val searchTextField: String = "",
    val showAddListSheet: Boolean = false,
    val addListTextField: String = "",
    val addListTextFieldHasError: Boolean = false,
)

data class ListItem (
    val number: Double = 1.0,
    val amountType: Amount = Amount.TIMES,
    val name: String = "",
    val checked: Boolean = false
)

enum class Amount(val text: String) {
    KG("kg"),
    ML("ml"),
    TIMES("x"),
    LITRES("l"),
    GRAMM("g"),
    PACKAGES("Packungen")
}