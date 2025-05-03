package com.example.einkaufsliste.model.repository

import com.example.einkaufsliste.model.models.ShoppingList
import com.example.einkaufsliste.model.models.toDomain
import com.example.einkaufsliste.model.models.toRealmObject
import com.example.einkaufsliste.model.realm.RealmShoppingItem
import com.example.einkaufsliste.model.realm.RealmShoppingList
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class ShoppingListRepository {
    private val configuration =
        RealmConfiguration.create(
            schema = setOf(
                RealmShoppingList::class,
                RealmShoppingItem::class
            )
        )
    private val realm = Realm.open(configuration)

    fun createShoppingList(shoppingList: ShoppingList) {
        realm.writeBlocking {
            copyToRealm(shoppingList.toRealmObject())
        }
    }

    fun getAllShoppingLists(): List<ShoppingList> {
        val lists: RealmResults<RealmShoppingList> = realm.query<RealmShoppingList>().find()
        return lists.toList().map { it.toDomain() }
    }

    fun getShoppingList(id: Int): ShoppingList {
        val list: RealmShoppingList = realm.query<RealmShoppingList>("id == $0", "$id").find().first()
        return list.toDomain()
    }

    fun updateShoppingItemCheckedState(listId: Int, itemId: Int, newState: Boolean) {
        realm.writeBlocking {
            val shoppingList = query<RealmShoppingList>("id == $0", "$listId").find().first()
            shoppingList.items.find { it.id == itemId }?.checked = newState
        }
    }
}