package com.example.einkaufsliste.viewmodel

import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.model.models.ShoppingList

data class ListViewModelState(
    val list: ShoppingList,
    val searchFieldOpen: Boolean = false,
    val searchFieldText: String = "",
    val shoppingItemToEdit: ShoppingItem? = null,
    val editShoppingItemText: String = "",
    val editAmountText: String = "",
    val editAmountMenuOpen: Boolean = false,
    val editAmountType: Amount = Amount.TIMES,
    val addShoppingItemText: String = "",
    val addAmountText: String = "",
    val addAmountMenuOpen: Boolean = false,
    val addAmountType: Amount = Amount.TIMES,
)
