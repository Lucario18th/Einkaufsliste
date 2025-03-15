package com.example.lists.domain.realm
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmShoppingList(
    @PrimaryKey
    val id: Int = 0,
    val items: RealmList<RealmShoppingItem> = emptyList<RealmShoppingItem>().toRealmList()
) : RealmObject {

}
