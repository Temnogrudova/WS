package com.example.websocketproject

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
data class ItemModel(
        @SerializedName("price")
        var price: String? = null)