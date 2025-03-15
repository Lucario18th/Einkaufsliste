package com.example.einkaufsliste.model.repository

import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.models.toRealmObject
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

    suspend fun createShoppingList(shoppingList: ShoppingList) {
        val list = shoppingList.toRealmObject()
        realm.write {
            copyToRealm(list)
        }
    }
}