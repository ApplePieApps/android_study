package com.dumber.study.viewmodel.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FoodModel {

    private var instance: Retrofit? = null

    val foodApiService: FoodApiService = getRetrofit().create(FoodApiService::class.java)

    private fun getRetrofit(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl("https://learn.codeit.kr/v2/1234/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build())
                .build()
        }
        return instance!!
    }


}