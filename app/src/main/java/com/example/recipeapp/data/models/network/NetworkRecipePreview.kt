package com.example.recipeapp.data.models.network

import com.example.recipeapp.data.models.RecipePreview
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
/**
 * Represents a recipe preview as received from the network API.
 *
 * This class is annotated with Moshi's `JsonClass` annotation to facilitate JSON
 * serialization and deserialization.
 *
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property image The URL to the image of the recipe.
 * @property imageType The type of the image.
 */
@JsonClass(generateAdapter = true)
data class NetworkRecipePreview(
    val id : Long,
    val title: String,
    val image: String,
    @Json(ignore = true)
    val imageType: String = ""
) {
    /**
     * Converts the network recipe preview into a [RecipePreview] object suitable for local
     * database storage.
     *
     * @return An instance of [RecipePreview] representing the recipe preview.
     */
    fun asDatabasePreviewModel() : RecipePreview {
        return RecipePreview(id, title, image)
    }
}
