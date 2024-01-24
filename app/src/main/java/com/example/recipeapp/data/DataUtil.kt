package com.example.recipeapp.data

/**
 * API key used to authenticate requests to the recipe API.
 */
const val API_KEY = "cef0cc7e504740c291e9d26f3f4e87a4"
/**
 * The default number of recipes to be fetched if a specific amount is not provided.
 */
const val RECIPE_AMOUNT_BY_DEFAULT = 10
/**
 * The maximum number of recipes that can be retrieved in a single API request.
 */
const val MAX_RECIPES_AMOUNT_AVAILABLE = 100
/**
 * The key used for storing and retrieving settings preferences in SharedPreferences.
 */
const val SETTINGS_KEY = "settings"
/**
 * The key used for storing and retrieving the selected recipe amount preference in SharedPreferences.
 */
const val RECIPE_AMOUNT_KEY = "recipeAmount"
/**
 * The argument key used for passing recipe information between fragments or activities.
 */
const val ARG_RECIPE = "recipe"
/**
 * The argument key used for passing ingredient information between fragments or activities.
 */
const val ARG_INGREDIENTS = "ingredients"
