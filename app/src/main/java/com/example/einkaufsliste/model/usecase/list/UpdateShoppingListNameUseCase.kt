package com.example.einkaufsliste.model.usecase.list

import com.example.einkaufsliste.model.repository.ShoppingListRepository

class UpdateShoppingListNameUseCase {
    private val repository = ShoppingListRepository()

    operator fun invoke(listId: Int, newName: String) {
        repository.updateShoppingListName(listId, newName)
    }
}