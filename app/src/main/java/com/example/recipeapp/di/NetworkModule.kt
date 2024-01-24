package com.example.recipeapp.di

import com.example.recipeapp.data.api.RecipeApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
/**
 * Base URL for the Spoonacular API.
 */
private const val BASE_URL = "https://api.spoonacular.com/recipes/"
/**
 * Dagger Hilt module for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides an instance of Moshi for JSON parsing.
     *
     * @return An instance of [Moshi].
     */
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    /**
     * Provides an instance of Retrofit for handling network requests.
     *
     * @param moshi The Moshi instance for JSON parsing.
     * @return An instance of [Retrofit].
     */
    @Provides
    fun provideRetrofit(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    /**
     * Provides the API service interface for accessing recipe data.
     *
     * @param retrofit The Retrofit instance for network communication.
     * @return An instance of [RecipeApi].
     */
    @Provides
    fun provideRecipeApi(retrofit: Retrofit) : RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }
}
