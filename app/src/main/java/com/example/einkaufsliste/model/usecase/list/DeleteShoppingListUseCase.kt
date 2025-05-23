package com.example.einkaufsliste.model.usecase.list

import com.example.einkaufsliste.model.repository.ShoppingListRepository

class DeleteShoppingListUseCase {
    private val repository = ShoppingListRepository()

    operator fun invoke(shoppingListId: Int) {
        repository.deleteShoppingList(shoppingListId)
    }
}