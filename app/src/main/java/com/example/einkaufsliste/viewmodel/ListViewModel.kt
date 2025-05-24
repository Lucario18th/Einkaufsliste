package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.model.usecase.item.CreateShoppingItemUseCase
import com.example.einkaufsliste.model.usecase.item.UpdateShoppingItemUseCase
import com.example.einkaufsliste.model.usecase.list.GetShoppingListUseCase
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

    fun changeEditItemDialogState(shoppingListToRename: ShoppingItem?) {
        listStateFlow.update { it.copy(shoppingItemToEdit = shoppingListToRename, editShoppingItemText = shoppingListToRename?.name?:  "") }
        makeValuesDefault()
    }

    fun updateEditTextField(text: String) {
        listStateFlow.update { it.copy(editShoppingItemText = text) }
    }

    fun updateEditAmountTextField(text: String) {
        listStateFlow.update { it.copy(editAmountText = text) }
    }

    fun createShoppingItem(name: String, amountType: Amount, number: String) {
        try {
            if (name != "") {
                createShoppingItemUseCase(name, amountType, number.toDouble(), shoppingList.id)
            }
        } catch (_: NumberFormatException) {
            createShoppingItemUseCase(name, amountType, 1.0, shoppingList.id)
        }
        updateShoppingList()
        makeValuesDefault()
    }

    fun makeValuesDefault() {
        listStateFlow.update {
            it.copy(
                editShoppingItemText = "",
                editAmountText = "",
                editAmountType = Amount.TIMES,
                addAmountText = "",
                addAmountType = Amount.TIMES,
                addShoppingItemText = ""
            )
        }
    }

    fun updateEditAmountMenuState(newState: Boolean) {
        listStateFlow.update { it.copy(editAmountMenuOpen = newState) }
    }

    fun updateEditAmountType(amountType: Amount) {
        listStateFlow.update { it.copy(editAmountType = amountType, editAmountMenuOpen = false) }
    }

    fun updateAddTextField(text: String) {
        listStateFlow.update { it.copy(addShoppingItemText = text) }
    }

    fun updateAddAmountTextField(text: String) {
        listStateFlow.update { it.copy(addAmountText = text) }
    }

    fun updateAddAmountMenuState(newState: Boolean) {
        listStateFlow.update { it.copy(addAmountMenuOpen = newState) }
    }

    fun updateAddAmountType(amountType: Amount) {
        listStateFlow.update { it.copy(addAmountType = amountType, addAmountMenuOpen = false) }
    }

    fun updateShoppingItem(name: String, amountType: Amount, number: String) {
        val shoppingItemToUpdate = listStateFlow.value.shoppingItemToEdit
        if (name != "" && shoppingItemToUpdate != null) {
            try {
                updateShoppingItemUseCase.updateShoppingItemValues(
                    shoppingListId.toInt(),
                    shoppingItemToUpdate.id,
                    name,
                    amountType,
                    number.toDouble()
                )
            } catch (_: NumberFormatException) {

                updateShoppingItemUseCase.updateShoppingItemValues(
                    shoppingListId.toInt(),
                    shoppingItemToUpdate.id,
                    name,
                    amountType,
                    1.0
                )
            }
        }
        updateShoppingList()
        makeValuesDefault()
    }

    fun updateItemMenuDropdownForId(id: Int?) {
        listStateFlow.update { it.copy(itemMenuDropdownOpenForId = id) }
    }

    fun updateDeleteItemDialogState(shoppingItemToDelete: ShoppingItem?) {
        listStateFlow.update { it.copy(shoppingItemToDelete = shoppingItemToDelete) }
    }
}