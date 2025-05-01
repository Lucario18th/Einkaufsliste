package com.example.einkaufsliste.model.repository

import com.example.einkaufsliste.model.realm.RealmShoppingItem
import com.example.einkaufsliste.model.realm.RealmShoppingList
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

    fun createShoppingList(shoppingList: RealmShoppingList) {
        realm.writeBlocking {
            copyToRealm(shoppingList)
        }
    }
}