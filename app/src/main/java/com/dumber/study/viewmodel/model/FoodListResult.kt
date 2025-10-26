package com.dumber.study.viewmodel.model

import com.google.gson.annotations.SerializedName

data class FoodItem(
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("updatedAt") val updatedAt: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("content") val content: String,
    @SerializedName("calorie") val calorie: Int,
)

data class FoodListResult(
    @SerializedName("foods") val foods: List<FoodItem>
)

