package com.dumber.study.viewmodel.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {

    @GET("foods")
    suspend fun getFoodList(
        @Query("search") search: String? = null,
        @Query("cursor") cursor: String? = null
    ): Response<FoodListResult>

}