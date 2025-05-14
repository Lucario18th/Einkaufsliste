package com.example.einkaufsliste.viewmodel

import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.model.models.ShoppingList

data class ListViewModelState(
    val list: ShoppingList,
    val searchFieldOpen: Boolean = false,
    val searchFieldText: String = "",
    val addEditItemDialogOpen: Boolean = false,
    val shoppingItemToEdit: ShoppingItem? = null,
    val addEditShoppingItemText: String = "",
    val amountText: String = "",
    val amountMenuOpen: Boolean = false,
    val chosenAmountType: Amount = Amount.TIMES
)