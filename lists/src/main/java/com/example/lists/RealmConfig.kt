package com.example.lists

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val configuration = RealmConfiguration.create(schema = setOf(ShoppingList::class, ShoppingItem::class))
val realm = Realm.open(configuration)