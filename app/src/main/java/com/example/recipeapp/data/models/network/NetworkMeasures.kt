package com.example.recipeapp.data.models.network

import com.squareup.moshi.JsonClass

/**
 * Represents different measurement values for a specific ingredient, including both
 * U.S. (imperial) and metric measurements. This class is designed to be used with Moshi
 * for JSON serialization and deserialization.
 *
 * @property us U.S. (imperial) measurement for the ingredient.
 * @property metric Metric measurement for the ingredient.
 */
@JsonClass(generateAdapter = true)
data class NetworkMeasures(
    val us: NetworkMeasurement,
    val metric: NetworkMeasurement
)