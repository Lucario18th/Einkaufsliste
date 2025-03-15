package com.example.lists.domain.models

import com.example.lists.domain.realm.RealmShoppingItem
import com.example.lists.domain.realm.RealmShoppingList
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList

data class ShoppingList(
    val id: Int,
    val items: List<ShoppingItem> = emptyList()
)

fun ShoppingList.toRealmObject(): RealmShoppingList {
    return RealmShoppingList(id, items.toRealmShoppingItemList())
}

fun RealmShoppingList.toDomain(): ShoppingList {
    return ShoppingList(id, items.toDomainShoppingItemList())
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