package com.example.einkaufsliste.model.models

import com.example.einkaufsliste.model.realm.RealmShoppingItem
import com.example.einkaufsliste.model.realm.RealmShoppingList
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList

data class ShoppingList(
    val id: Int,
    val name: String,
    val items: List<ShoppingItem> = emptyList()
)

fun ShoppingList.toRealmObject(): RealmShoppingList {
    return RealmShoppingList(id, name,  items.toRealmShoppingItemList())
}

fun RealmShoppingList.toDomain(): ShoppingList {
    return ShoppingList(id, name,  items.toDomainShoppingItemList())
}

fun ShoppingList.completion(): Int {
    var itemCount = 0
    items.forEach {
        it.checked?: itemCount++
    }
    return itemCount
}

private fun List<ShoppingItem>.toRealmShoppingItemList():RealmList<RealmShoppingItem> {
    val newList: MutableList<RealmShoppingItem> = mutableListOf()
    this.forEach { shoppingItem ->
        newList.add(shoppingItem.toRealmObject())
    }
    return newList.toRealmList()
}

private fun RealmList<RealmShoppingItem>.toDomainShoppingItemList():List<ShoppingItem> {
    val newList: MutableList<ShoppingItem> = mutableListOf()
    this.forEach { realmShoppingItem ->
        newList.add(realmShoppingItem.toDomain())
    }
    return newList.toList()
}