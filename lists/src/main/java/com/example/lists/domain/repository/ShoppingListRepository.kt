package com.example.lists.domain.repository

import com.example.lists.domain.models.RealmShoppingItem
import com.example.lists.domain.models.RealmShoppingList
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class ShoppingListRepository {
    private val configuration =
        RealmConfiguration.create(schema = setOf(RealmShoppingList::class, RealmShoppingItem::class))
    private val realm = Realm.open(configuration)

    fun createShoppingList() {

    }
}