package com.example.einkaufsliste.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmShoppingItem() : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var amountType: String = "x"
    var number: Double = 1.0
    var checked: Boolean = false
    constructor(
        id: Int = 0,
        name: String,
        amountType: String = "x",
        number: Double = 1.0,
        checked: Boolean = false
    ) : this() {
        this.id = id
        this.name = name
        this.amountType = amountType
        this.number = number
        this.checked = checked
    }
}