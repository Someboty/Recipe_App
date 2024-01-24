package com.example.recipeapp.data.models.network

import com.example.recipeapp.data.models.Ingredient
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
/**
 * Represents an ingredient as received from the network API.
 *
 * This class is annotated with Moshi's `JsonClass` and `Json` annotations to facilitate JSON
 * serialization and deserialization.
 *
 * @property id The unique identifier of the ingredient.
 * @property aisle The aisle in the store where the ingredient is typically found.
 * @property image The URL to an image of the ingredient.
 * @property consistency The consistency of the ingredient (e.g., "solid," "liquid").
 * @property name The name of the ingredient.
 * @property nameClean A clean version of the ingredient's name.
 * @property original The original representation of the ingredient.
 * @property originalName The original name of the ingredient.
 * @property amount The amount of the ingredient.
 * @property unit The unit of measurement for the ingredient's amount.
 * @property meta Additional meta information about the ingredient.
 * @property measures List of [NetworkMeasures] representing different measures for the ingredient.
 */
@JsonClass(generateAdapter = true)
data class NetworkIngredient (
    val id: Long,
    @Json(ignore = true)
    val aisle: String = "",
    @Json(ignore = true)
    val image: String = "",
    @Json(ignore = true)
    val consistency: String = "",
    val name: String,
    @Json(ignore = true)
    val nameClean: String = "",
    @Json(ignore = true)
    val original: String = "",
    @Json(ignore = true)
    val originalName: String = "",
    val amount: Double,
    val unit: String,
    @Json(ignore = true)
    val meta: List<String> = ArrayList(),
    @Json(ignore = true)
    val measures: List<NetworkMeasures> = ArrayList()
) {
    /**
     * Converts the network ingredient into a database model [Ingredient].
     *
     * @return An instance of [Ingredient] representing the ingredient for local database storage.
     */
    fun asDataBaseModel() : Ingredient {
        return Ingredient(
            ingredientId = id,
            name = name,
            amount = amount,
            unit = unit
        )
    }
}
