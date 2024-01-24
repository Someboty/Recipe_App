package com.example.recipeapp.data.repository

import com.example.recipeapp.data.API_KEY
import com.example.recipeapp.data.MAX_RECIPES_AMOUNT_AVAILABLE
import com.example.recipeapp.data.api.RecipeApi
import com.example.recipeapp.data.db.RecipeCharacteristicDao
import com.example.recipeapp.data.db.RecipeDetailedDao
import com.example.recipeapp.data.db.RecipePreviewDao
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.data.models.RecipePreview
import com.example.recipeapp.data.models.RecipeWithIngredients
import com.example.recipeapp.data.models.network.NetworkRecipeContainer
import com.example.recipeapp.data.models.network.NetworkRecipeDetailed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * The maximum number of recipes that should be retrieved in a API requests.
 */
private const val MAX_SHORT_RECIPES_SAVED = 1000
/**
 * Repository class responsible for managing recipe data from both local database and remote API.
 *
 * @property recipeDetailedDao Instance of [RecipeDetailedDao] for accessing detailed recipe data in the local database.
 * @property recipePreviewDao Instance of [RecipePreviewDao] for accessing recipe preview data in the local database.
 * @property recipeApi Instance of [RecipeApi] for making requests to the remote recipe API.
 */
class RecipeRepository(
    private val recipeDetailedDao : RecipeDetailedDao,
    private val recipePreviewDao : RecipePreviewDao,
    private val recipeCharacteristicDao: RecipeCharacteristicDao,
    private val recipeApi: RecipeApi
) {
    /**
     * Retrieves a list of recipe previews from the local database or the API if not enough data is available.
     *
     * @param amount The desired number of recipe previews.
     * @return List of [RecipePreview] objects.
     */
    suspend fun getRecipesPreviews(amount: Int) : List<RecipePreview> {
        return withContext(Dispatchers.IO) {
            try {
                var savedRecipes = getPreviewsFromDb(amount)
                Timber.i("Got " + savedRecipes.size + " from db")
                if (savedRecipes.size < amount) {
                    Timber.i("Need $amount recipes, going to update db")
                    saveMoreRecipesPreviewsToDb()
                    savedRecipes = getPreviewsFromDb(amount)
                    Timber.i("Got " + savedRecipes.size + " from db after updating")
                }
                savedRecipes
            } catch (e: Exception) {
                Timber.e(e.message + "\n" + " " + e.cause?.message)
                emptyList()
            }
        }
    }
    /**
     * Retrieves detailed information about a recipe from the local database or the API if not available locally.
     *
     * @param recipeId The unique identifier of the recipe.
     * @return An instance of [RecipeWithIngredients] if the recipe is found, otherwise null.
     */
    suspend fun getRecipeDetailed(recipeId: Long) : RecipeWithIngredients? {
        return withContext(Dispatchers.IO) {
            var recipe = getRecipeDetailedFromDb(recipeId)
            if (recipe == null) {
                Timber.i("Can't find recipe with id $recipeId in database.\n" +
                        " Trying to get it from api")
                addRecipeDetailsToDb(recipeId)
                recipe = getRecipeDetailedFromDb(recipeId)
            }
            Timber.i("Received recipe with id ${recipe?.recipe?.recipeId} from database")
            recipe
        }
    }
    /**
     * Retrieves a list of favorite recipes from the local database.
     *
     * @return List of [RecipeDetailed] objects marked as favorites.
     */
    suspend fun getFavoriteRecipes() : List<RecipeDetailed> {
        Timber.i("Trying to get favorite recipes from database")
        return withContext(Dispatchers.IO) {
            recipeCharacteristicDao.getFavorites()
        }
    }
    /**
     * Updates the favorite status of a recipe in the local database.
     *
     * @param recipeDetailed The detailed recipe to be updated.
     */
    suspend fun updateFavoriteStatus(recipeDetailed: RecipeDetailed) {
        if (recipeDetailed.isFavorite) {
            Timber.i("Removing recipe ${recipeDetailed.recipeId} from favorites")
        } else {
            Timber.i("Adding recipe ${recipeDetailed.recipeId} to favorites")
        }
        withContext(Dispatchers.IO) {
            recipeCharacteristicDao.addToFavorites(recipeDetailed)
        }
    }
    /**
     * Retrieves a list of recipes matching a given name from the local database or the API if not enough data is available.
     *
     * @param name The name of the recipes to search for.
     * @param amount The desired number of recipes.
     * @return List of [RecipePreview] objects matching the search criteria.
     */
    suspend fun getRecipesByName(name: String, amount: Int) : List<RecipePreview> {
        Timber.i("Trying to get recipes by name $name from database")
        return withContext(Dispatchers.IO) {
            var recipes = recipePreviewDao.getRecipesByName(name, amount)
            if (recipes.size < amount) {
                Timber.i("Received ${recipes.size} recipes from database, while $amount is needed\n" +
                        "Trying to get recipes by name $name from API")
                addRecipesByNameToDb(name, amount)
                recipes = recipePreviewDao.getRecipesByName(name, amount)
            }
            recipes
        }

    }
    /**
     * Retrieves a detailed recipe with ingredients from the local database.
     *
     * @param id The unique identifier of the recipe.
     * @return An instance of [RecipeWithIngredients] if the recipe is found, otherwise null.
     */
    private suspend fun getRecipeDetailedFromDb(id: Long): RecipeWithIngredients? {
        return withContext(Dispatchers.IO) {
            recipeDetailedDao.getRecipeWithIngredients(id)
        }
    }
    /**
     * Retrieves a list of recipe previews from the local database.
     *
     * @param amount The desired number of recipe previews.
     * @return List of [RecipePreview] objects.
     */
    private suspend fun getPreviewsFromDb(amount: Int): List<RecipePreview> {
        return recipePreviewDao.getRecipesPreviews(amount)
    }
    /**
     * Saves 1000 recipe previews to the local database in batches to avoid exceeding the maximum allowed limit.
     */
    private suspend fun saveMoreRecipesPreviewsToDb() {
        withContext(Dispatchers.IO) {
            val batches = MAX_SHORT_RECIPES_SAVED / MAX_RECIPES_AMOUNT_AVAILABLE

            val deferredJobs = (0 until batches).map { batch ->
                async {
                    val offset = batch * MAX_RECIPES_AMOUNT_AVAILABLE
                    val recipes = recipeApi.getRecipesPreviews(
                        API_KEY,
                        MAX_RECIPES_AMOUNT_AVAILABLE * batches,
                        offset
                    )
                    Timber.i("Got ${recipes.results.size} recipes from API at offset $offset")
                    saveRecipesContainerToDb(recipes)
                }
            }

            deferredJobs.forEach { it.await() }
        }
    }
    /**
     * Saves a recipe with ingredients to the local database.
     *
     * @param recipes The recipe to be saved.
     */
    private suspend fun insertRecipeWithIngredients(recipeWithIngredients: RecipeWithIngredients) {
        withContext(Dispatchers.IO) {
            recipeDetailedDao.insertRecipeWithIngredients(
                recipeWithIngredients.recipe,
                recipeWithIngredients.ingredients
            )
        }
    }

    /**
     * Retrieves detailed information about a recipe from the API and saves it to the local database.
     *
     * @param recipeId The unique identifier of the recipe.
     */
    private suspend fun addRecipeDetailsToDb(recipeId: Long) {
        withContext(Dispatchers.IO) {
            try {
                val recipeFromNetwork = fetchRecipeDetailsFromApi(recipeId)
                    .asDatabaseDetailedModelWithRecipes()
                Timber.i("Trying to save recipe $recipeId details to database")
                insertRecipeWithIngredients(recipeFromNetwork)
            } catch (e: Exception) {
                Timber.e("Failed to add recipe details to db.\nCause: ${e.message}")
            }
        }
    }
    /**
     * Retrieves recipe previews by name from the API and saves them to the local database.
     *
     * @param name The name of the recipes to search for.
     * @param amount The desired number of recipes.
     */
    private suspend fun addRecipesByNameToDb(name: String, amount: Int) {
        withContext(Dispatchers.IO) {
            val recipes = recipeApi.getRecipesPreviewsByQuery(
                API_KEY, name, amount
            )
            Timber.i("Got " + recipes.results.size + " recipes by name $name from api")
            saveRecipesContainerToDb(recipes)
        }
    }
    /**
     * Saves a batch of recipe previews to the local database.
     *
     * @param recipes The batch of recipes to be saved.
     */
    private suspend fun saveRecipesContainerToDb(recipes: NetworkRecipeContainer) {
        withContext(Dispatchers.IO) {
            recipePreviewDao.insertRecipePreviews(recipes.asDatabasePreviewModel())
        }
    }
    /**
     * Retrieves detailed information about a recipe from the API.
     *
     * @param recipeId The unique identifier of the recipe.
     * @return An instance of [NetworkRecipeDetailed] representing the detailed recipe information.
     */
    private suspend fun fetchRecipeDetailsFromApi(recipeId: Long): NetworkRecipeDetailed {
        Timber.i("Trying to get recipe $recipeId details from API")
        return withContext(Dispatchers.IO) {
            recipeApi.getRecipeInformation(recipeId, API_KEY)
        }
    }
}
