package com.example.einkaufsliste.model.realm
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmShoppingList() : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var items: RealmList<RealmShoppingItem> = emptyList<RealmShoppingItem>().toRealmList()
    constructor(
        id: Int,
        name: String,
        items: RealmList<RealmShoppingItem> = emptyList<RealmShoppingItem>().toRealmList()
    ) : this() {
        this.id = id
        this.name = name
        this.items = items
    }
}
