package com.example.websocketproject

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemModel(val price: String?)