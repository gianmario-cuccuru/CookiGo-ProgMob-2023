package com.example.cookigo.data.network

import com.example.cookigo.models.FoodRecipe
import com.example.cookigo.models.FoodTrivia
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/recipes/complexSearch/")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipe>

    @GET("food/trivia/random")
    suspend fun getFoodTrivia(
        @Query("apiKey") apiKey: String
    ): Response<FoodTrivia>
}