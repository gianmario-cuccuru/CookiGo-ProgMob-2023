package com.example.cookigo.data

import com.example.cookigo.data.network.FoodRecipesApi
import com.example.cookigo.models.FoodRecipe
import com.example.cookigo.models.FoodTrivia
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe>{
         return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe>{
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodTrivia(apiKey: String): Response<FoodTrivia>{
        return foodRecipesApi.getFoodTrivia(apiKey)
    }
}