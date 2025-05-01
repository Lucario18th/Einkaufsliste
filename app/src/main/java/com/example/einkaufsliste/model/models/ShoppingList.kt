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

fun ShoppingList.completionRate(): Float {
    var itemCount = 0
    items.forEach {
        if (it.checked) itemCount++
    }
    if (itemCount == 0 || items.isEmpty()) return 0f

    return items.size / itemCount.toFloat()
}

fun ShoppingList.completion(): Int {
    var itemCount = 0
    items.forEach { if (it.checked) itemCount++ }
    return itemCount
}

fun getTestList(): ShoppingList {
    val list = mutableListOf<ShoppingItem>()
    list.add(ShoppingItem(1, "test", Amount.KG, 1, true))
    list.add(ShoppingItem(2, "test1", Amount.LITRES, 1, true))
    list.add(ShoppingItem(3, "test2", Amount.ML, 1, true))
    list.add(ShoppingItem(4, "test3", Amount.PACKAGES, 1, false))
    return ShoppingList(1, "testlist", list)
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