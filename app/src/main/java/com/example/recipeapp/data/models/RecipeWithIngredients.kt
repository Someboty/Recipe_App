package com.example.recipeapp.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
/**
 * Represents a combination of a detailed recipe and its associated ingredients for local
 * database storage.
 *
 * @property recipe An instance of [RecipeDetailed] representing the detailed recipe information.
 * @property ingredients List of [Ingredient] objects associated with the recipe.
 */
@Parcelize
data class RecipeWithIngredients(
    @Embedded val recipe: RecipeDetailed,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientId",
        associateBy = Junction(RecipeIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
) : Parcelable
