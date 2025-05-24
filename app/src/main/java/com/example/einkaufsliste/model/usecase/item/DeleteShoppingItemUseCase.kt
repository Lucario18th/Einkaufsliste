package com.example.einkaufsliste.model.usecase.item

import com.example.einkaufsliste.model.repository.ShoppingListRepository

class DeleteShoppingItemUseCase {
    private val repository = ShoppingListRepository()

    operator fun invoke(itemId: Int) {
        repository.deleteShoppingItem(itemId)
    }
}