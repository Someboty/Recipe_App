package com.example.recipeapp.ui.details.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.models.Ingredient
import com.example.recipeapp.data.models.RecipeDetailed
/**
 * ViewModel responsible for managing recipe ingredients displayed in the UI.
 */
class DetailsIngredientsViewModel : ViewModel() {
    // LiveData for observing recipe details
    private val _ingredients = MutableLiveData<List<Ingredient>>()
    val ingredients : LiveData<List<Ingredient>> get() = _ingredients
    /**
     * Sets the received recipe details to be displayed.
     *
     * @param ingredients The list of [Ingredient] objects containing recipe ingredients.
     */
    fun getRecipeDetails(ingredients : List<Ingredient>) {
        _ingredients.value = ingredients
    }
}
