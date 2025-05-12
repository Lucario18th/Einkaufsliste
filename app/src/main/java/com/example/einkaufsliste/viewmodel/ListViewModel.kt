package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.model.usecase.item.CreateShoppingItemUseCase
import com.example.einkaufsliste.model.usecase.list.GetShoppingListUseCase
import com.example.einkaufsliste.model.usecase.item.UpdateShoppingItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListViewModel  (
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val getShoppingListUseCase: GetShoppingListUseCase = GetShoppingListUseCase()
    private val updateShoppingItemUseCase: UpdateShoppingItemUseCase = UpdateShoppingItemUseCase()
    private val createShoppingItemUseCase: CreateShoppingItemUseCase = CreateShoppingItemUseCase()

    private val shoppingListId = savedStateHandle.get<String>("shoppingListId") ?: throw IllegalArgumentException("Keine Id für Shoppinglist bekommen")
    private val shoppingList = getShoppingListUseCase.getShoppingList(shoppingListId.toInt())

    private val listStateFlow = MutableStateFlow(ListViewModelState(shoppingList))
    val listViewState = listStateFlow.asStateFlow()

    private fun updateShoppingList() {
        val shoppingList = getShoppingListUseCase.getShoppingList(shoppingListId.toInt())
        listStateFlow.update { it.copy(list = shoppingList) }
    }

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

    fun toggleShoppingItemState(shoppingItem: ShoppingItem) {
        updateShoppingItemUseCase.updateShoppingItemCheckedState(shoppingListId.toInt(),shoppingItem.id, !shoppingItem.checked)
        listStateFlow.update {
            it.copy(list = getShoppingListUseCase.getShoppingList(shoppingListId.toInt()))
        }
    }

    fun changeAddEditItemDialogState(state: Boolean, shoppingListToRename: ShoppingItem? = null) {
        listStateFlow.update { it.copy(addEditItemDialogOpen = state, shoppingItemToEdit = shoppingListToRename, addEditShoppingItemText = shoppingListToRename?.name?:  "") }
    }

    fun updateAddEditTextField(text: String) {
        listStateFlow.update { it.copy(addEditShoppingItemText = text) }
    }

    fun updateAmountTextField(text: String) {
        listStateFlow.update { it.copy(amountText = text) }
    }

    fun createShoppingItem(name: String, amountType: Amount, number: Int) {
        createShoppingItemUseCase(name, amountType, number, shoppingList.id)
        updateShoppingList()
    }
}