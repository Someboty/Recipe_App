package com.example.recipeapp.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import com.example.recipeapp.data.models.Ingredient
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.data.models.RecipeIngredientCrossRef
import com.example.recipeapp.data.models.RecipePreview
import com.example.recipeapp.data.models.RecipeWithIngredients
import timber.log.Timber
/**
 * Data Access Object (DAO) for recipe previews.
 *
 * This DAO provides methods for retrieving and inserting recipe preview data.
 *
 */
@Dao
interface RecipePreviewDao {
    /**
     * Retrieves a specified number of random RecipePreviews from the 'recipes_short' table.
     *
     * @param amount The number of recipe previews to retrieve.
     * @return A list of RecipePreview
     */
    @Query("SELECT * FROM recipes_short ORDER BY RANDOM() LIMIT :amount")
    suspend fun getRecipesPreviews(amount: Int) : List<RecipePreview>
    /**
     * Inserts a list of RecipePreviews into the 'recipes_short' table,
     * ignoring duplicates based on conflict strategy.
     *
     * @param recipes The list of RecipePreview to insert.
     */
    @Transaction
    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun insertRecipePreviews(recipes: List<RecipePreview>)
    /**
     * Retrieves a specified number of random RecipePreviews from the 'recipes_short' table
     * whose titles contain the given name.
     *
     * @param name The name to match in the recipe previews.
     * @param amount The number of recipe previews to retrieve.
     * @return A list of RecipePreview matching the name.
     */
    @Query("SELECT * FROM recipes_short r WHERE r.title LIKE '%' || :name || '%' ORDER BY RANDOM() LIMIT :amount")
    suspend fun getRecipesByName(name: String, amount: Int) : List<RecipePreview>
}
/**
 * Data Access Object (DAO) for detailed recipes.
 *
 * This DAO provides methods for retrieving, inserting, and managing detailed recipe data,
 * including ingredients and cross-references.
 *
 */
@Dao
interface RecipeDetailedDao {
    /**
     * Retrieves a RecipeWithIngredients object containing detailed recipe information
     * and a list of associated ingredients based on the provided recipeId.
     *
     * @param recipeId The ID of the recipe to retrieve.
     * @return A [RecipeWithIngredients] object containing the detailed recipe and its ingredients.
     */
    @Transaction
    suspend fun getRecipeWithIngredients(recipeId: Long) : RecipeWithIngredients? {
        val recipe = getRecipeDetails(recipeId)
        Timber.d("Received recipe with id ${recipe?.recipeId}")
        if (recipe == null) {
            Timber.e("Recipe was not found")
            return null
        }
        val ingredients = getRecipeIngredients(recipeId)
        Timber.d("Received list of ingredients of recipe id ${recipe.recipeId}. Count: ${ingredients.size}")
        return RecipeWithIngredients(recipe, ingredients)
    }
    /**
     *  Inserts a RecipeWithIngredients object into the database, ensuring atomicity.
     *
     * @param recipe The [RecipeDetailed] object containing the detailed recipe.
     * @param ingredients The list of [Ingredient] containing all Ingredients of this recipe.
     */
    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: RecipeDetailed, ingredients: List<Ingredient>) {
        insertRecipeDetailed(recipe)
        ingredients.forEach { ingredient ->
            insertIngredientWithCrossRef(ingredient, recipe)
        }
    }
    /**
     * Inserts an ingredient and the corresponding cross-reference with a recipe using a transaction.
     *
     * @param ingredient The ingredient to insert.
     * @param recipe The detailed recipe to associate the ingredient with.
     */
    @Transaction
    suspend fun insertIngredientWithCrossRef(ingredient: Ingredient, recipe: RecipeDetailed) {
        checkAndInsertIngredient(ingredient)

        checkAndInsertCrossRef(recipe, ingredient)
    }
    /**
     * Checks for the existence of a cross-reference between a recipe and an ingredient.
     * If not found, inserts a new cross-reference using a transaction.
     *
     * @param recipe The detailed recipe.
     * @param ingredient The ingredient.
     */
    @Transaction
    suspend fun checkAndInsertCrossRef(
        recipe: RecipeDetailed,
        ingredient: Ingredient
    ) {
        val existingCrossRef = getRecipeIngredientCrossRef(recipe.recipeId, ingredient.ingredientId)
        if (existingCrossRef == null) {
            insertRecipeIngredientCrossRef(
                RecipeIngredientCrossRef(
                    recipe.recipeId,
                    ingredient.ingredientId
                )
            )
        }
    }
    /**
     * Checks for the existence of an ingredient by name. If not found, inserts a new ingredient.
     *
     * @param ingredient The ingredient to check and insert.
     */
    @Transaction
    suspend fun checkAndInsertIngredient(ingredient: Ingredient) {
        if (getIngredientByName(ingredient.name) == null) {
            insertIngredient(ingredient)
        }
    }
    /**
     * Inserts a new cross-reference between a recipe and an ingredient.
     *
     * @param recipeIngredientCrossRef The cross-reference entity to insert.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipeIngredientCrossRef(recipeIngredientCrossRef: RecipeIngredientCrossRef)
    /**
     * Inserts a new ingredient into the database, ignoring conflicts.
     *
     * @param ingredient The ingredient to insert.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIngredient(ingredient: Ingredient)
    /**
     * Retrieves an ingredient by name.
     *
     * @param name The name of the ingredient.
     * @return The matching ingredient, or null if not found.
     */
    @Query("SELECT * FROM ingredients i WHERE i.name = :name")
    suspend fun getIngredientByName(name: String): Ingredient?
    /**
     * Inserts a detailed recipe into the database, ignoring conflicts.
     *
     * @param recipe The detailed recipe to insert.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipeDetailed(recipe: RecipeDetailed)
    /**
     * Retrieves a detailed recipe by ID.
     *
     * @param recipeId The ID of the recipe.
     * @return The detailed recipe, or null if not found.
     */
    @Query("SELECT * FROM recipes_detailed r WHERE r.recipeId = :recipeId")
    suspend fun getRecipeDetails(recipeId: Long) : RecipeDetailed?
    /**
     * Retrieves a list of ingredients for a specific recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return A list of ingredients for the specified recipe.
     */
    @Query("SELECT * FROM recipe_ingredient_cross_ref r_i JOIN ingredients i ON r_i.ingredientId = i.ingredientId WHERE r_i.recipeId = :recipeId")
    suspend fun getRecipeIngredients(recipeId: Long) : List<Ingredient>
    /**
     * Retrieves a cross-reference entity for a specific recipe and ingredient.
     *
     * @param recipeId The ID of the recipe.
     * @param ingredientId The ID of the ingredient.
     * @return The cross-reference entity, or null if not found.
     */
    @Query("SELECT * FROM recipe_ingredient_cross_ref WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    suspend fun getRecipeIngredientCrossRef(recipeId: Long, ingredientId: Long): RecipeIngredientCrossRef?
}
@Dao
interface RecipeCharacteristicDao {
    /**
     * Retrieves a list of favorite recipes.
     *
     * @return A list of favorite recipes.
     */
    @Query("SELECT * FROM recipes_detailed r WHERE r.isFavorite = true")
    suspend fun getFavorites() : List<RecipeDetailed>
    /**
     * Marks a specific recipe as a favorite in the 'recipes_detailed' table.
     *
     * @param recipeDetailed The detailed recipe to mark as a favorite.
     */
    @Transaction
    @Update
    suspend fun addToFavorites(recipeDetailed: RecipeDetailed)
}

/**
 * Room Database class for storing recipe-related data.
 *
 * This database class includes entities for recipe previews, detailed recipes, ingredients,
 * and the cross-reference table connecting recipes and ingredients.
 *
 * @property recipePreviewDao Data Access Object for recipe previews.
 * @property recipeDetailedDao Data Access Object for detailed recipes.
 * @property recipeCharacteristicDao Data Access Object for characteristics of recipes.
 */
@Database(entities = [RecipePreview::class, RecipeDetailed::class, Ingredient::class, RecipeIngredientCrossRef::class], version = 6)
abstract class RecipeDatabase : RoomDatabase() {
    /**
     * Provides access to the RecipePreviewDao for interacting with recipe preview data.
     */
    abstract val recipePreviewDao : RecipePreviewDao
    /**
     * Provides access to the RecipeDetailedDao for interacting with detailed recipe data.
     */
    abstract val recipeDetailedDao : RecipeDetailedDao
    /**
     * Provides access to the RecipeCharacteristicDao for interacting with characteristics of recipes data.
     */
    abstract val recipeCharacteristicDao: RecipeCharacteristicDao
}
