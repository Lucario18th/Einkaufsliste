package com.example.lists.domain.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ShoppingItem() : RealmObject {
    @PrimaryKey
    val id: Int = 0
    val name: String = ""
    val amountType: String = ""
    var number: Int = 1
    var checked: Boolean = false
}