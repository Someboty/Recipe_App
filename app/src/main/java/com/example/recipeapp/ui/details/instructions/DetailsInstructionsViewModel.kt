package com.example.recipeapp.ui.details.instructions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.models.RecipeDetailed
/**
 * ViewModel responsible for managing recipe instructions displayed in the UI.
 */
class DetailsInstructionsViewModel : ViewModel() {
    // LiveData for observing recipe details
    private val _recipe = MutableLiveData<RecipeDetailed>()
    val recipe : LiveData<RecipeDetailed> get() = _recipe
    /**
     * Sets the received recipe details to be displayed.
     *
     * @param recipeReceived The RecipeDetailed object containing recipe details.
     */
    fun getRecipeDetails(recipeReceived : RecipeDetailed) {
        _recipe.value = recipeReceived
    }
}
