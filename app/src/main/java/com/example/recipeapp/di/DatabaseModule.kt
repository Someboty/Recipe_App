package com.example.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipeapp.data.db.RecipeDatabase
import com.example.recipeapp.data.repository.RecipeRepository
import com.example.recipeapp.data.api.RecipeApi
import com.example.recipeapp.data.db.RecipeCharacteristicDao
import com.example.recipeapp.data.db.RecipeDetailedDao
import com.example.recipeapp.data.db.RecipePreviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
/**
 * Dagger Hilt module for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Provides the application's Room database instance.
     *
     * @param context The application context.
     * @return An instance of [RecipeDatabase].
     */
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : RecipeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeDatabase::class.java,
            "recipes")
            .fallbackToDestructiveMigration()
            .build()
    }
    /**
     * Provides the Data Access Object (DAO) for recipe previews.
     *
     * @param recipeDatabase The Room database instance.
     * @return An instance of [RecipePreviewDao].
     */
    @Provides
    fun provideRecipePreviewDao(recipeDatabase: RecipeDatabase): RecipePreviewDao {
        return recipeDatabase.recipePreviewDao
    }
    /**
     * Provides the Data Access Object (DAO) for detailed recipes.
     *
     * @param recipeDatabase The Room database instance.
     * @return An instance of [RecipeDetailedDao].
     */
    @Provides
    fun provideRecipeDetailedDao(recipeDatabase: RecipeDatabase): RecipeDetailedDao {
        return recipeDatabase.recipeDetailedDao
    }
    /**
     * Provides the Data Access Object (DAO) for characteristics of recipes.
     *
     * @param recipeDatabase The Room database instance.
     * @return An instance of [RecipeCharacteristicDao].
     */
    @Provides
    fun provideRecipeCharacteristicDao(recipeDatabase: RecipeDatabase) : RecipeCharacteristicDao {
        return recipeDatabase.recipeCharacteristicDao
    }
    /**
     * Provides the repository for managing recipe data.
     *
     * @param recipeDetailedDao The DAO for detailed recipes.
     * @param recipePreviewDao The DAO for recipe previews.
     * @param recipeApi The API service for retrieving recipe data.
     * @return An instance of [RecipeRepository].
     */
    @Provides
    fun provideRecipeRepository(
        recipeDetailedDao : RecipeDetailedDao,
        recipePreviewDao : RecipePreviewDao,
        recipeCharacteristicDao: RecipeCharacteristicDao,
        recipeApi: RecipeApi) : RecipeRepository {
        return RecipeRepository(
            recipeDetailedDao,
            recipePreviewDao,
            recipeCharacteristicDao,
            recipeApi
        )
    }
}
