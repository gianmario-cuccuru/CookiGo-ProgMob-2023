package com.example.cookigo

import com.example.cookigo.data.network.FoodRecipesApi
import com.example.cookigo.models.ExtendedIngredient
import com.example.cookigo.models.FoodRecipe
import com.example.cookigo.models.FoodTrivia
import com.example.cookigo.models.Result
import retrofit2.Response

class FakeFoodRecipesApi : FoodRecipesApi {
    override suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {

        val fakeFoodRecipe = createFakeFoodRecipe()

        return Response.success(fakeFoodRecipe)
    }

    override suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {

        val fakeSearchResult = createFakeFoodRecipe()

        return Response.success(fakeSearchResult)
    }

    override suspend fun getFoodTrivia(apiKey: String): Response<FoodTrivia> {

        val fakeTrivia = FoodTrivia("Food Trivia")
            return Response.success(fakeTrivia)
    }

    private fun createFakeFoodRecipe(): FoodRecipe {
        val extendedIngredient = ExtendedIngredient(
            amount = 1.5,
            image = "https://example.com/ingredient.jpg",
            name = "Ingredient",
            original = "Description",
            unit = "Cup"
        )

        val result = Result(
            aggregateLikes = 42,
            cheap = true,
            dairyFree = false,
            extendedIngredients = listOf(extendedIngredient),
            glutenFree = true,
            id = 123,
            image = "https://example.com/image.jpg",
            readyInMinutes = 30,
            sourceName = "Food Blog",
            sourceUrl = "https://example.com/recipe",
            summary = "Recipe description",
            title = "Recipe",
            vegan = false,
            vegetarian = true,
            veryHealthy = true
        )

        return FoodRecipe(results = listOf(result))
    }
}