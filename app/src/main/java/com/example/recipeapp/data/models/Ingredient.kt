package com.example.recipeapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
/**
 * Represents an ingredient entity for local database storage.
 *
 * @property ingredientId The unique identifier of the ingredient.
 * @property name The name of the ingredient.
 * @property amount The numerical value representing the amount of the ingredient.
 * @property unit The unit of measurement for the ingredient.
 */
@Entity(tableName = "ingredients")
@Parcelize
data class Ingredient (
    @PrimaryKey
    val ingredientId: Long,
    val name: String,
    val amount: Double,
    var unit: String
) : Parcelable
