package com.example.lists.domain.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmShoppingItem(
    @PrimaryKey val id: Int,
    val name: String,
    val amountType: String,
    var number: Int = 1,
    var checked: Boolean = false,
) : RealmObject {
}