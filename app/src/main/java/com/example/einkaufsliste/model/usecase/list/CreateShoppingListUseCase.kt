package com.example.einkaufsliste.model.usecase.list

import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.repository.ShoppingListRepository

class CreateShoppingListUseCase(
    private val repository: ShoppingListRepository = ShoppingListRepository()
) {
    operator fun invoke(shoppingList: ShoppingList) {
        repository.createShoppingList(shoppingList)
    }
}