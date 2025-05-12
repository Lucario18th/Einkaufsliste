package com.example.einkaufsliste.model.usecase.item

import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
import com.example.einkaufsliste.model.repository.ShoppingListRepository
import com.example.einkaufsliste.model.usecase.GetRandomKeyValueUseCase

class CreateShoppingItemUseCase {
    private val repository = ShoppingListRepository()
    private val getRandomKeyValueUseCase = GetRandomKeyValueUseCase()

    operator fun invoke(name: String, amountType: Amount, number: Int, listId: Int) {
        val shoppingItem = ShoppingItem(
            id = getRandomKeyValueUseCase(),
            name = name,
            amountType = amountType,
            number = number
        )
        repository.createShoppingItem(shoppingItem, listId)
    }
}