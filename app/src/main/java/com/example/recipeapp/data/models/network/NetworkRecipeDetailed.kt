package com.example.recipeapp.data.models.network

import com.example.recipeapp.data.models.Ingredient
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.data.models.RecipeWithIngredients
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
/**
 * Represents a detailed recipe as received from the network API.
 *
 * This class is annotated with Moshi's `JsonClass` annotation to facilitate JSON
 * serialization and deserialization.
 *
 * @property vegetarian Indicates whether the recipe is vegetarian.
 * @property vegan Indicates whether the recipe is vegan.
 * @property glutenFree Indicates whether the recipe is gluten-free.
 * @property dairyFree Indicates whether the recipe is dairy-free.
 * @property veryHealthy Indicates whether the recipe is considered very healthy.
 * @property cheap Indicates whether the recipe is considered cheap.
 * @property veryPopular Indicates whether the recipe is very popular (ignored during serialization).
 * @property sustainable Indicates whether the recipe is sustainable (ignored during serialization).
 * @property lowFodmap Indicates whether the recipe is low FODMAP (ignored during serialization).
 * @property weightWatcherSmartPoints The SmartPoints value for the recipe (ignored during serialization).
 * @property gaps Information related to GAPS (Gut and Psychology Syndrome) diet (ignored during serialization).
 * @property preparationMinutes The time required for preparation (ignored during serialization).
 * @property cookingMinutes The time required for cooking (ignored during serialization).
 * @property aggregateLikes The total number of likes for the recipe.
 * @property healthScore The health score of the recipe (ignored during serialization).
 * @property creditsText Credits text for the recipe (ignored during serialization).
 * @property sourceName The source name for the recipe (ignored during serialization).
 * @property pricePerServing The price per serving for the recipe (ignored during serialization).
 * @property extendedIngredients List of [NetworkIngredient] objects representing the extended ingredients of the recipe.
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property readyInMinutes The total time required for the recipe.
 * @property servings The number of servings for the recipe (ignored during serialization).
 * @property sourceUrl The source URL for the recipe (ignored during serialization).
 * @property image The URL to the image of the recipe.
 * @property imageType The type of the image (ignored during serialization).
 * @property summary A summary description of the recipe.
 * @property cuisines List of cuisines associated with the recipe (ignored during serialization).
 * @property dishTypes List of dish types associated with the recipe (ignored during serialization).
 * @property diets List of diets associated with the recipe.
 * @property occasions List of occasions associated with the recipe (ignored during serialization).
 * @property winePairing Information related to wine pairing (ignored during serialization).
 * @property instructions The cooking instructions for the recipe.
 * @property analyzedInstructions List of analyzed instructions associated with the recipe (ignored during serialization).
 * @property originalId The original identifier of the recipe (ignored during serialization).
 * @property spoonacularScore The Spoonacular score for the recipe (ignored during serialization).
 * @property spoonacularSourceUrl The Spoonacular source URL for the recipe (ignored during serialization).
 */
@JsonClass(generateAdapter = true)
data class NetworkRecipeDetailed(
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val veryHealthy: Boolean,
    val cheap: Boolean,
    @Json(ignore = true)
    val veryPopular: Boolean = false,
    @Json(ignore = true)
    val sustainable: Boolean = false,
    @Json(ignore = true)
    val lowFodmap: Boolean = false,
    @Json(ignore = true)
    val weightWatcherSmartPoints: Int = -1,
    @Json(ignore = true)
    val gaps: String = "",
    @Json(ignore = true)
    val preparationMinutes: Int = -1,
    @Json(ignore = true)
    val cookingMinutes: Int = -1,
    val aggregateLikes: Int,
    @Json(ignore = true)
    val healthScore: Int = -1,
    @Json(ignore = true)
    val creditsText: String = "",
    @Json(ignore = true)
    val sourceName: String = "",
    @Json(ignore = true)
    val pricePerServing: Double = -1.0,
    val extendedIngredients: List<NetworkIngredient>,
    val id: Long,
    val title: String,
    val readyInMinutes: Int,
    @Json(ignore = true)
    val servings: Int = -1,
    @Json(ignore = true)
    val sourceUrl: String = "",
    val image: String,
    @Json(ignore = true)
    val imageType: String = "",
    val summary: String,
    @Json(ignore = true)
    val cuisines: List<String> = ArrayList(),
    @Json(ignore = true)
    val dishTypes: List<String> = ArrayList(),
    val diets: List<String>,
    @Json(ignore = true)
    val occasions: List<String> = ArrayList(),
    @Json(ignore = true)
    val winePairing: String? = "",
    val instructions: String,
    @Json(ignore = true)
    val analyzedInstructions: List<String> = ArrayList(),
    @Json(ignore = true)
    val originalId: Long? = -1,
    @Json(ignore = true)
    val spoonacularScore: Double = -1.0,
    @Json(ignore = true)
    val spoonacularSourceUrl: String = ""
) {
    /**
     * Converts the network recipe detailed data into a [RecipeWithIngredients] object
     * suitable for local database storage.
     *
     * @return An instance of [RecipeWithIngredients] representing the detailed recipe with its ingredients.
     */
    fun asDatabaseDetailedModelWithRecipes() : RecipeWithIngredients {
        return RecipeWithIngredients(
            RecipeDetailed(
                recipeId = id,
                title = title,
                summary = summary,
                instructions = instructions,
                isVegetarian = vegetarian,
                isVegan = vegan,
                isDairyFree =  dairyFree,
                isVeryHealthy =  veryHealthy,
                isCheap =  cheap,
                isGlutenFree =  glutenFree,
                readyInMinutes = readyInMinutes,
                isFavorite = false,
                likes = aggregateLikes,
                image = image,
            ),
            parseIngredients(extendedIngredients)
        )
    }
    /**
     * Parses the list of network ingredients into a list of [Ingredient] objects.
     *
     * @param networkIngredients List of [NetworkIngredient] representing extended ingredients.
     * @return List of [Ingredient] objects.
     */
    private fun parseIngredients(networkIngredients: List<NetworkIngredient>) : List<Ingredient> {
        return networkIngredients.map { ingredient ->
            ingredient.asDataBaseModel()
        }
    }
}
