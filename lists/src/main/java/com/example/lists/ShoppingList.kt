package com.example.lists
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ShoppingList() : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var items: RealmList<ShoppingItem> = emptyList<ShoppingItem>().toRealmList()
}
