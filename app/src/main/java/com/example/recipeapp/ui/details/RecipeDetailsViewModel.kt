package com.example.recipeapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.data.models.RecipeWithIngredients
import com.example.recipeapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {
    // LiveData for the detailed recipe information
    private val _recipe = MutableLiveData<RecipeWithIngredients?>()
    val recipe : LiveData<RecipeWithIngredients?> get() = _recipe
    // LiveData for tracking the favorite status of the recipe
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean> get() = _isFavorite
    // LiveData for signaling if there was an issue fetching the recipe
    private val _recipeFailed = MutableLiveData<Boolean>()
    val recipeFailed : LiveData<Boolean> get() = _recipeFailed
    /**
     * Fetches detailed information for a specific recipe by its ID.
     *
     * @param recipeId The ID of the recipe to fetch.
     */
    fun getRecipeDetails(recipeId : Long) {
        viewModelScope.launch {
            try {
                // Attempt to retrieve the detailed recipe from the repository
                val recipeFromDb = repository.getRecipeDetailed(recipeId)
                if (recipeFromDb != null) {
                    _recipe.value = recipeFromDb
                    updateFavoriteInfo(recipeFromDb.recipe)
                    Timber.d("Recipe successfully received. Recipe id: ${_recipe.value?.recipe?.recipeId}")
                }
            } catch (e: Exception) {
                // Log an error if fetching the recipe details fails
                Timber.e("Failed to get recipe by id " + recipeId + ", " + e.message)
                failedToReceiveRecipe()
            }
        }
    }
    /**
     * Toggles the favorite status of a recipe.
     *
     * @param recipeDetailed The detailed information of the recipe.
     */
    fun changeFavorite(recipeDetailed: RecipeDetailed) {
        val recipeUpdated = reverseFavorite(recipeDetailed)
        viewModelScope.launch {
            try {
                // Attempt to update the favorite status in the repository
                repository.updateFavoriteStatus(recipeUpdated)
                updateFavoriteInfo(recipeUpdated)
                Timber.d("Recipe successfully updated. Recipe id: ${_recipe.value?.recipe?.recipeId}")
            } catch (e: Exception) {
                // Log an error if updating the favorite status fails
                Timber.e("Failed to add recipe to favorite by id " + recipeDetailed.recipeId + ", " + e.message)
                e.printStackTrace()
            }
        }
    }
    /**
     * Resets the flag indicating a failure to fetch the recipe.
     */
    fun failedToReceiveRecipeCompleted() {
        _recipeFailed.value = false
    }
    /**
     * Signals that there was a failure to fetch the recipe.
     */
    private fun failedToReceiveRecipe() {
        _recipeFailed.value = true
    }
    /**
     * Toggles the favorite status of a recipe.
     *
     * @param recipeDetailed The detailed information of the recipe.
     * @return The recipe with the updated favorite status.
     */
    private fun reverseFavorite(recipeDetailed: RecipeDetailed) : RecipeDetailed {
        recipeDetailed.isFavorite = !recipeDetailed.isFavorite
        return recipeDetailed
    }
    /**
     * Updates the LiveData with the current favorite status.
     *
     * @param recipeFromDb The detailed information of the recipe.
     */
    private fun updateFavoriteInfo(recipeFromDb: RecipeDetailed) {
        _isFavorite.value = recipeFromDb.isFavorite
    }
}
