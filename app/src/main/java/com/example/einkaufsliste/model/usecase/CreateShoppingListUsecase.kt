package com.example.einkaufsliste.model.usecase

import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.repository.ShoppingListRepository

class CreateShoppingListUsecase(
    private val repository: ShoppingListRepository = ShoppingListRepository()
) {
    suspend operator fun invoke(shoppingList: ShoppingList) {
        repository.createShoppingList(shoppingList)
    }
}