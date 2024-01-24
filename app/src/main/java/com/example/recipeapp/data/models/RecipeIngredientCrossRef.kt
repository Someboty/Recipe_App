package com.example.recipeapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
/**
 * Represents a cross-reference table between recipes and ingredients for local
 * database storage.
 *
 * This class is annotated with the `@Entity` annotation to define it as a Room entity
 * and `@Parcelize` annotation to facilitate Parcelable implementation.
 *
 * @property recipeId The unique identifier of the associated recipe.
 * @property ingredientId The unique identifier of the associated ingredient.
 */
@Entity(tableName = "recipe_ingredient_cross_ref", primaryKeys = ["recipeId", "ingredientId"])
@Parcelize
data class RecipeIngredientCrossRef(
    val recipeId: Long,
    val ingredientId: Long
) : Parcelable
