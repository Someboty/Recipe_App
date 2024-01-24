package com.example.recipeapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RECIPE_AMOUNT_BY_DEFAULT
import com.example.recipeapp.data.models.RecipePreview
import com.example.recipeapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel class for managing UI-related data for the HomeFragment.
 *
 * @param repository The repository providing access to recipe data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {
    // LiveData to observe the list of recipes to display on the UI
    private val _recipes = MutableLiveData<List<RecipePreview>>()
    val recipes: LiveData<List<RecipePreview>> get() = _recipes
    // LiveData to observe the recipe ID for navigation to details screen
    private val _navigateToRecipeDetails = MutableLiveData<Long?>()
    val navigateToRecipeDetails: LiveData<Long?> get() = _navigateToRecipeDetails
    // LiveData to observe the chosen recipe amount for display
    private val _recipesAmount = MutableLiveData<Int>()
    val recipesAmount: LiveData<Int> get() = _recipesAmount
    /**
     * Initiates navigation to the recipe details screen.
     *
     * @param recipeId The ID of the selected recipe.
     */
    fun navigateToRecipeDetails(recipeId: Long) {
        Timber.d("Navigation to recipe details was triggered for recipeId: $recipeId.")
        _navigateToRecipeDetails.value = recipeId
    }
    /**
     * Completes the navigation to the recipe details screen.
     */
    fun navigateToRecipeDetailsCompleted() {
        Timber.d("Navigation to recipe details was completed.")
        _navigateToRecipeDetails.value = null
    }
    /**
     * Fetches all recipes from the repository based on the selected recipe amount.
     */
    fun getAllRecipes() {
        Timber.d("Fetching recipes from the repository.")
        viewModelScope.launch {
            try {
                _recipes.value = repository.getRecipesPreviews(
                    recipesAmount.value ?: RECIPE_AMOUNT_BY_DEFAULT
                )
                Timber.d("Recipes list successfully updated. Count: ${_recipes.value?.size}.")
            } catch (e: Exception) {
                Timber.e("Failed to update recipes list: ${e.message}.")
                _recipes.value = emptyList()
            }
        }
    }
    /**
     * Updates the LiveData with the chosen recipe amount.
     *
     * @param amount The chosen recipe amount.
     */
    fun updateAmount(amount: Int) {
        Timber.d("Updating recipes amount. New amount: $amount.")
        _recipesAmount.value = amount
    }
    /**
     * Fetches recipes from the repository based on the specified name and the selected recipe amount.
     *
     * @param name The name used for filtering recipes.
     */
    fun getRecipesByQuery(name: String) {
        Timber.d("Fetching recipes by name $name from the repository.")
        viewModelScope.launch {
            try {
                _recipes.value = repository.getRecipesByName(
                    name, _recipesAmount.value ?: RECIPE_AMOUNT_BY_DEFAULT
                )
                Timber.d("Recipes by name $name list successfully updated. " +
                        "Count: ${_recipes.value?.size}.")
            } catch (e: Exception) {
                Timber.w("Failed to update recipes list by name $name: ${e.message}.")
                _recipes.value = emptyList()
            }
        }
    }
}
