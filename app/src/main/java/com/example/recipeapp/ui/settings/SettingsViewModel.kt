package com.example.recipeapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.MAX_RECIPES_AMOUNT_AVAILABLE
/**
 * The minimum number of recipes that can be shown in UI.
 */
private const val MIN_RECIPES_AMOUNT_AVAILABLE = 1
/**
 * ViewModel class for managing application settings related to the number of recipes to display.
 * The minimum number of recipes that can be shown in the UI is [MIN_RECIPES_AMOUNT_AVAILABLE].
 */
class SettingsViewModel : ViewModel() {
    // LiveData to observe if the entered recipe amount is incorrect
    private val _amountIncorrect = MutableLiveData<Boolean>()
    val amountIncorrect: LiveData<Boolean> = _amountIncorrect

    // LiveData to observe the chosen recipe amount
    private val _recipeAmount = MutableLiveData<Int>()
    val recipeAmount : LiveData<Int> = _recipeAmount

    /**
     * Checks if the entered recipe amount is within the valid range.
     *
     * @param amount The entered recipe amount.
     * @return True if the amount is within the valid range, false otherwise.
     */
    fun checkRecipeAmount(amount: Int) : Boolean {
        return if (amount < MIN_RECIPES_AMOUNT_AVAILABLE || amount > MAX_RECIPES_AMOUNT_AVAILABLE) {
            snackbarMessage()
            false
        } else true
    }
    /**
     * Resets the snackbar message flag when it has been displayed.
     */
    fun snackbarMessageCompleted() {
        _amountIncorrect.value = false
    }
    /**
     * Displays the snackbar message flag when the entered recipe amount is incorrect.
     */
    fun snackbarMessage() {
        _amountIncorrect.value = true
    }
    /**
     * Updates the LiveData with the chosen recipe amount.
     *
     * @param amount The chosen recipe amount.
     */
    fun updateAmount(amount: Int) {
        _recipeAmount.value = amount
    }
}
