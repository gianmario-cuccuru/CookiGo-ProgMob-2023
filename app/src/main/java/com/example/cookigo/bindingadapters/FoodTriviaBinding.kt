package com.example.cookigo.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.cookigo.models.FoodTrivia
import com.example.cookigo.util.NetworkResult

class FoodTriviaBinding {
    companion object{

        @BindingAdapter("readApiResponse3")
        @JvmStatic
        fun setCardVisibility(
            view: View,
            apiResponse: NetworkResult<FoodTrivia>?
        ){
            when(apiResponse){
                is NetworkResult.Loading -> {
                    view.visibility = View.INVISIBLE
                }
                is NetworkResult.Error -> {
                    view.visibility = View.INVISIBLE
                }
                is NetworkResult.Success -> {
                    view.visibility = View.VISIBLE
                }
                else -> {view.visibility = View.INVISIBLE}
            }
        }

        @BindingAdapter("readApiResponse4")
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodTrivia>?
        ){
            when(apiResponse) {
                is NetworkResult.Loading -> {
                    view.visibility = View.INVISIBLE
                }
                is NetworkResult.Error -> {
                    view.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    view.visibility = View.INVISIBLE
                }
                else -> {
                    view.visibility = View.INVISIBLE
                }
            }
        }
    }
}