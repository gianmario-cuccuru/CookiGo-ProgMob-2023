package com.example.cookigo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookigo.data.DataStoreRepository
import com.example.cookigo.data.MealAndDietType
import com.example.cookigo.util.Constants.Companion.API_KEY
import com.example.cookigo.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.cookigo.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.cookigo.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.cookigo.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.cookigo.util.Constants.Companion.QUERY_API_KEY
import com.example.cookigo.util.Constants.Companion.QUERY_DIET
import com.example.cookigo.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.cookigo.util.Constants.Companion.QUERY_NUMBER
import com.example.cookigo.util.Constants.Companion.QUERY_SEARCH
import com.example.cookigo.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
    ): AndroidViewModel(application) {

    lateinit var mealAndDiet: MealAndDietType
    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipesViewModel::mealAndDiet.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealAndDiet.selectedMealType,
                    mealAndDiet.selectedMealTypeId,
                    mealAndDiet.selectedDietType,
                    mealAndDiet.selectedDietTypeId
                )
            }
        }

    fun saveMealAndDietTypeTemp(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        mealAndDiet = MealAndDietType(
            mealType,
            mealTypeId,
            dietType,
            dietTypeId
        )
    }

    fun applyQueries(): HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        if (this@RecipesViewModel::mealAndDiet.isInitialized) {
            queries[QUERY_TYPE] = mealAndDiet.selectedMealType
            queries[QUERY_DIET] = mealAndDiet.selectedDietType
        } else {
            queries[QUERY_TYPE] = DEFAULT_MEAL_TYPE
            queries[QUERY_DIET] = DEFAULT_DIET_TYPE
        }

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}