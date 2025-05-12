package com.example.einkaufsliste.model.usecase.list

import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.repository.ShoppingListRepository

class GetShoppingListUseCase(
    private val repository: ShoppingListRepository = ShoppingListRepository()
) {
    fun getAllShoppingLists(): List<ShoppingList> {
        return repository.getAllShoppingLists()
    }

    fun getShoppingList(id: Int): ShoppingList {
        return repository.getShoppingList(id)
    }
}