package com.example.lists.domain.usecase

import com.example.lists.domain.repository.ShoppingListRepository

class CreateShoppingListUsecase(
    val repository: ShoppingListRepository = ShoppingListRepository()
) {
    fun invoke() {

    }
}