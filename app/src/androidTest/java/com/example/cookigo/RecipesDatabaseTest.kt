package com.example.cookigo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cookigo.data.database.RecipesDao
import com.example.cookigo.data.database.RecipesDatabase
import com.example.cookigo.data.database.entities.FavoritesEntity
import com.example.cookigo.data.database.entities.RecipesEntity
import com.example.cookigo.models.ExtendedIngredient
import com.example.cookigo.models.FoodRecipe
import com.example.cookigo.models.Result
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RecipesDatabaseTest : TestCase(){

    private lateinit var db: RecipesDatabase
    private lateinit var dao: RecipesDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java).build()
        dao = db.recipesDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun insertAndReadRecipes() = runBlocking{
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

        val foodRecipe = FoodRecipe(
            results = listOf(
                result
            )
        )

        val recipesEntity = RecipesEntity(foodRecipe)
        dao.insertRecipes(recipesEntity)
        val recipesList = dao.readRecipes().first()
        Assert.assertEquals(1, recipesList.size)
    }

    @Test
    fun insertReadAndDeleteFavoriteRecipes() = runBlocking{
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

        val favoritesEntity = FavoritesEntity(
            id = 1,
            result = result
        )

        dao.insertFavoriteRecipe(favoritesEntity)
        val recipesList = dao.readFavoriteRecipes().first()
        Assert.assertEquals(1, recipesList.size)
        dao.deleteFavoriteRecipe(favoritesEntity)
        val recipesListAfterDelete = dao.readFavoriteRecipes().first()
        Assert.assertEquals(0, recipesListAfterDelete.size)
    }
}