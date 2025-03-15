package com.example.lists.domain.models

import com.example.lists.domain.realm.RealmShoppingItem

data class ShoppingItem(
    val id: Int,
    val name: String,
    val amountType: String,
    val number: Int = 1,
    val checked: Boolean = false,
)

fun ShoppingItem.toRealmObject(): RealmShoppingItem {
    return RealmShoppingItem(id, name, amountType, number, checked)
}

fun RealmShoppingItem.toDomain(): ShoppingItem {
    return ShoppingItem(id, name, amountType, number, checked)
}