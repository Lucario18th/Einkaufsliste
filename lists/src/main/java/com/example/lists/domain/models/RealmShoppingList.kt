package com.example.lists.domain.models
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmShoppingList() : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var items: RealmList<RealmShoppingItem> = emptyList<RealmShoppingItem>().toRealmList()
}
