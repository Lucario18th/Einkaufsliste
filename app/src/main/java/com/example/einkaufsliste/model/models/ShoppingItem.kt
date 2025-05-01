package com.example.einkaufsliste.model.models

import com.example.einkaufsliste.model.realm.RealmShoppingItem

data class ShoppingItem(
    val id: Int,
    val name: String,
    val amountType: Amount = Amount.TIMES,
    val number: Int = 1,
    val checked: Boolean = false,
)

fun ShoppingItem.toRealmObject(): RealmShoppingItem {
    return RealmShoppingItem(id, name, amountType.text, number, checked)
}

fun RealmShoppingItem.toDomain(): ShoppingItem {
    return ShoppingItem(id, name, amountType.toAmount(), number, checked)
}

fun String.toAmount(): Amount {
    return when(this) {
        "kg" -> Amount.KG
        "ml" -> Amount.ML
        "x" -> Amount.TIMES
        "l" -> Amount.LITRES
        "g" -> Amount.GRAMM
        "Packungen" -> Amount.PACKAGES
        else -> throw IllegalArgumentException("String $this konnte nicht in Amount geparsed werden")
    }
}