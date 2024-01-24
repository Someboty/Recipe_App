package com.example.recipeapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
/**
 * Represents a detailed recipe entity for local database storage.
 *
 * This class is annotated with the `@Entity` annotation to define it as a Room entity
 * and `@Parcelize` annotation to facilitate Parcelable implementation.
 *
 * @property recipeId The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property summary A summary description of the recipe.
 * @property instructions The cooking instructions for the recipe.
 * @property isVegetarian Indicates whether the recipe is vegetarian (1 for true, 0 for false).
 * @property isVegan Indicates whether the recipe is vegan (1 for true, 0 for false).
 * @property isGlutenFree Indicates whether the recipe is gluten-free (1 for true, 0 for false).
 * @property isDairyFree Indicates whether the recipe is dairy-free (1 for true, 0 for false).
 * @property isVeryHealthy Indicates whether the recipe is considered very healthy (1 for true, 0 for false).
 * @property isCheap Indicates whether the recipe is considered cheap (1 for true, 0 for false).
 * @property readyInMinutes The total time required for the recipe.
 * @property isFavorite Indicates whether the recipe is marked as a favorite (1 for true, 0 for false).
 * @property likes The total number of likes for the recipe.
 * @property image The URL to the image of the recipe.
 */
@Entity(tableName = "recipes_detailed")
@Parcelize
data class RecipeDetailed (
    @PrimaryKey
    val recipeId: Long,
    val title: String,
    val summary: String,
    val instructions: String?,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val isGlutenFree: Boolean,
    val isDairyFree: Boolean,
    val isVeryHealthy: Boolean,
    val isCheap: Boolean,
    val readyInMinutes: Int,
    var isFavorite: Boolean,
    var likes: Int,
    val image: String,
) : Parcelable
