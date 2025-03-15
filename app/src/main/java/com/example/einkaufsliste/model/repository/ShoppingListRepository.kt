package com.example.lists.domain.repository

import com.example.lists.domain.models.ShoppingList
import com.example.lists.domain.models.toRealmObject
import com.example.lists.domain.realm.RealmShoppingItem
import com.example.lists.domain.realm.RealmShoppingList
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class ShoppingListRepository {
    private val configuration =
        RealmConfiguration.create(
            schema = setOf(
                RealmShoppingList::class,
                RealmShoppingItem::class
            )
        )
    private val realm = Realm.open(configuration)

    suspend fun createShoppingList(shoppingList: ShoppingList) {
        val list = shoppingList.toRealmObject()
        realm.write {
            copyToRealm(list)
        }
    }
}