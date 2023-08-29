package com.example.cookigo.models


import com.google.gson.annotations.SerializedName

data class FoodTrivia(
    @SerializedName("text")
    val text: String
)