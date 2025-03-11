package com.example.cookigo.viewmodels

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cookigo.data.LocalDataSource
import com.example.cookigo.data.RemoteDataSource
import com.example.cookigo.data.Repository
import com.example.cookigo.data.database.entities.RecipesEntity
import com.example.cookigo.models.ExtendedIngredient
import com.example.cookigo.models.FoodRecipe
import com.example.cookigo.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: Repository
    @Mock
    lateinit var application: Application

    private lateinit var viewModel: MainViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val extendedIngredient = ExtendedIngredient(
        amount = 1.5,
        image = "https://example.com/ingredient.jpg",
        name = "Ingredient",
        original = "Description",
        unit = "Cup"
    )

    private val result = Result(
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

    private val foodRecipe = FoodRecipe(
        results = listOf(
            result
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = MainViewModel(repository, application)
    }

    @Test
    fun testInsertRecipes() = runTest{
        val recipesEntity = RecipesEntity(foodRecipe)

        viewModel.insertRecipes(recipesEntity)

        Mockito.verify(repository).local.insertRecipes(recipesEntity)
    }
}
