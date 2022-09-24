package com.mrntlu.bisu.service

import com.mrntlu.bisu.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object{
        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}