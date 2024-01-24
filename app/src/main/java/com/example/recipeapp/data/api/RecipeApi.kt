package com.example.recipeapp.data.api

import com.example.recipeapp.data.models.network.NetworkRecipeDetailed
import com.example.recipeapp.data.models.network.NetworkRecipeContainer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
/**
 * Interface defining the API endpoints for retrieving recipe data.
 */
interface RecipeApi {
    /**
     * Retrieves a list of recipe previews.
     *
     * @param key The API key for authentication.
     * @param recipeAmount The number of recipes to retrieve.
     * @param offset The offset for paginated results.
     * @return A container of network recipe previews.
     */
    @GET("complexSearch")
    suspend fun getRecipesPreviews(
        @Query("apiKey") key : String,
        @Query("number") recipeAmount: Int,
        @Query("offset") offset: Int
    ) : NetworkRecipeContainer
    /**
     * Retrieves a list of recipe previews based on a query.
     *
     * @param key The API key for authentication.
     * @param name The query string for recipe names.
     * @param recipeAmount The number of recipes to retrieve.
     * @return A container of network recipe previews.
     */
    @GET("complexSearch")
    suspend fun getRecipesPreviewsByQuery(
        @Query("apiKey") key : String,
        @Query("query") name: String,
        @Query("number") recipeAmount: Int
    ): NetworkRecipeContainer
    /**
     * Retrieves detailed information for a specific recipe.
     *
     * @param recipeId The ID of the recipe to retrieve information for.
     * @param apiKey The API key for authentication.
     * @return Detailed information about the specified recipe.
     */
    @GET("{recipeId}/information")
    suspend fun getRecipeInformation(
        @Path("recipeId") recipeId: Long,
        @Query("apiKey") apiKey: String
    ): NetworkRecipeDetailed
}
