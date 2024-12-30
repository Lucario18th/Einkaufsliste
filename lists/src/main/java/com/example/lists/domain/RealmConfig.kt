package com.example.lists.domain

import com.example.lists.domain.models.ShoppingItem
import com.example.lists.domain.models.ShoppingList
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val configuration = RealmConfiguration.create(schema = setOf(ShoppingList::class, ShoppingItem::class))
val realm = Realm.open(configuration)