package com.example.websocketproject

import com.google.gson.annotations.SerializedName

data class GroceryModel(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("bagColor")
        var bagColor: String? = null,
        @SerializedName("weight")
        var weight: String? = null
)