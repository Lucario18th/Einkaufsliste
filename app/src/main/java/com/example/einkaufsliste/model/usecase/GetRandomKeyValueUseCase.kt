package com.example.einkaufsliste.model.usecase

import kotlin.random.Random

class GetRandomKeyValueUseCase {
    operator fun invoke(): Int {
        // Aktuelle Zeit in Millisekunden seit Epoche
        val currentTimeMillis = System.currentTimeMillis()

        // Zufallszahl zwischen 1000 und 9999
        val randomSuffix = Random.nextInt(1000, 9999)

        // Kombinieren von Zeit und Zufallszahl
        val primaryKey = (currentTimeMillis % 1000000).toInt() * 10000 + randomSuffix

        return primaryKey
    }
}