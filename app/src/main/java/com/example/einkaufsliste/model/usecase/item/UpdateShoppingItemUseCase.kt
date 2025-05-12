package com.example.einkaufsliste.model.usecase.item

import com.example.einkaufsliste.model.repository.ShoppingListRepository

class UpdateShoppingItemUseCase {
    private val repository: ShoppingListRepository = ShoppingListRepository()

    fun updateShoppingItemCheckedState(shoppingListId: Int, shoppingItemId: Int, newState: Boolean) {
        repository.updateShoppingItemCheckedState(shoppingListId, shoppingItemId, newState)
    }
}