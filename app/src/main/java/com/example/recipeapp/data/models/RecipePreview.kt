package com.example.recipeapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
/**
 * Represents a short preview of a recipe entity for local database storage.
 *
 * This class is annotated with the `@Entity` annotation to define it as a Room entity
 * and `@Parcelize` annotation to facilitate Parcelable implementation.
 *
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property image The URL to the image of the recipe.
 */
@Entity(tableName = "recipes_short")
@Parcelize
data class RecipePreview (
    @PrimaryKey
    val id: Long,
    val title: String,
    val image: String,
) : Parcelable
