package com.example.recipeapp.ui.details.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.R
import com.example.recipeapp.data.models.RecipeDetailed
import timber.log.Timber
/**
 * ViewModel responsible for managing recipe overview displayed.
 */
class DetailsOverviewViewModel : ViewModel() {
    // LiveData for observing recipe details
    private val _recipe = MutableLiveData<RecipeDetailed>()
    val recipe : LiveData<RecipeDetailed>
        get() = _recipe
    /**
     * Sets the received recipe details to be displayed.
     *
     * @param recipeReceived The RecipeDetailed object containing recipe details.
     */
    fun getRecipeDetails(recipeReceived : RecipeDetailed) {
        Timber.i("Received recipe with id ${recipeReceived.recipeId}")
        _recipe.value = recipeReceived
    }
    /**
     * Determines the correct checkmark image based on the given characteristic value.
     *
     * @param characteristic The characteristic value (0 or 1) indicating the presence or absence of a feature.
     * @return The resource ID of the corresponding checkmark image.
     */
    fun setCorrectCheckImage(characteristic: Boolean) : Int {
        return if (characteristic) R.drawable.ic_details_characteristics_true
            else R.drawable.ic_details_characteristics_false
    }
    /**
     * Determines the correct text color for a characteristic based on the given value.
     *
     * @param characteristic The characteristic value (0 or 1) indicating the presence or absence of a feature.
     * @return The resource ID of the corresponding color.
     */
    fun setCorrectCharacteristicColor(characteristic: Boolean) : Int {
        return if (characteristic) R.color.characteristic_true_green
            else R.color.characteristic_false_grey
    }
}
