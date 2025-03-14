package com.example.cookigo.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import androidx.lifecycle.*
import com.example.cookigo.data.Repository
import com.example.cookigo.data.database.entities.FavoritesEntity
import com.example.cookigo.data.database.entities.RecipesEntity
import com.example.cookigo.models.FoodRecipe
import com.example.cookigo.models.FoodTrivia
import com.example.cookigo.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    var recyclerViewState: Parcelable? = null

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()

    fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }


    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodTriviaResponse: MutableLiveData<NetworkResult<FoodTrivia>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    fun getFoodTrivia(apiKey: String) = viewModelScope.launch {
        getFoodTriviaSafeCall(apiKey)
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null){
                    offlineCacheResponse(foodRecipe)
                }
            }catch (e: Exception){
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            }catch (e: Exception){
                searchRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        }else{
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getFoodTriviaSafeCall(apiKey: String) {
        foodTriviaResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getFoodTrivia(apiKey)
                foodTriviaResponse.value = handleFoodTriviaResponse(response)
            }catch (e: Exception){
                foodTriviaResponse.value = NetworkResult.Error("Recipes not found.")
            }
        }else{
            foodTriviaResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheResponse(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleFoodTriviaResponse(response: Response<FoodTrivia>): NetworkResult<FoodTrivia>? {
        return when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val foodTrivia = response.body()
                return NetworkResult.Success(foodTrivia!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    internal fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}