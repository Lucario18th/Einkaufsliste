package com.example.einkaufsliste.model.repository

import com.example.einkaufsliste.model.models.Amount
import com.example.einkaufsliste.model.models.ShoppingItem
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

    fun updateShoppingListName(listId: Int, newName: String) {
        realm.writeBlocking {
            val shoppingList = query<RealmShoppingList>("id == $0", "$listId").find().first()
            shoppingList.name = newName
        }
    }

    fun createShoppingItem(shoppingItem: ShoppingItem, listId: Int) {
        realm.writeBlocking {
            val shoppingList = query<RealmShoppingList>("id == $0", "$listId").find().first()
            shoppingList.items.add(shoppingItem.toRealmObject())
        }
    }

    fun updateShoppingItem(listId: Int, itemId: Int, newName: String, newAmountType: Amount, newNumber: Double) {
        realm.writeBlocking {
            val shoppingList = query<RealmShoppingList>("id == $0", "$listId").find().first()
            val itemToUpdate = shoppingList.items.find { it.id == itemId }
            itemToUpdate?.apply {
                name = newName
                amountType = newAmountType.text
                number = newNumber
            }
        }
    }

    fun deleteShoppingList(listId: Int) {
        realm.writeBlocking {
            val shoppingList = query<RealmShoppingList>("id == $0", "$listId").find().first()
            val listItems = shoppingList.items
            listItems.forEach { item ->
                delete(item)
            }

            val shoppingListToDelete = findLatest(shoppingList)
            if (shoppingListToDelete != null) {
                delete(shoppingListToDelete)
            }
        }
    }

    fun deleteShoppingItem(itemId: Int) {
        realm.writeBlocking {
            val shoppingItem = query<RealmShoppingItem>("id == $0", "$itemId").find().first()
            delete(shoppingItem)
        }
    }
}