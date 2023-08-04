package com.example.cookigo.models

import com.google.gson.annotations.SerializedName

data class ExtendedIngredient (

    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String
)