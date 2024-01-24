package com.example.recipeapp.data.models.network

import com.example.recipeapp.data.models.RecipePreview
import com.squareup.moshi.JsonClass
/**
 * Represents a container for a list of recipe previews received from the network API.
 *
 * This class is annotated with Moshi's `JsonClass` annotation to facilitate JSON
 * serialization and deserialization.
 *
 * @property results List of [NetworkRecipePreview] objects representing recipe previews.
 * @property offset The offset value indicating the starting position of the results in the entire data set.
 * @property number The number of items returned in the current response.
 * @property totalResults The total number of results available from the API.
 */
@JsonClass(generateAdapter = true)
data class NetworkRecipeContainer(
    val results: List<NetworkRecipePreview>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
) {
    /**
     * Converts the network recipe container into a list of database model [RecipePreview] objects.
     *
     * @return A list of [RecipePreview] objects representing the recipe previews for local database storage.
     */
    fun asDatabasePreviewModel() : List<RecipePreview> {
        return results.map {
            it.asDatabasePreviewModel()
        }
    }
}
