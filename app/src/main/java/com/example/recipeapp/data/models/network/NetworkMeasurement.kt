package com.example.recipeapp.data.models.network

import com.squareup.moshi.JsonClass
/**
 * Represents a measurement as received from the network API.
 *
 * This class is annotated with Moshi's `JsonClass` annotation to facilitate JSON
 * serialization and deserialization.
 *
 * @property amount The numerical value representing the amount of the measurement.
 * @property unitShort The short form of the measurement unit.
 * @property unitLong The long form of the measurement unit.
 */
@JsonClass(generateAdapter = true)
data class NetworkMeasurement(
    val amount: Double,
    val unitShort: String,
    val unitLong: String
)
