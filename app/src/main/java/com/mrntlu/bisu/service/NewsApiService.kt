package com.mrntlu.bisu.service

import com.mrntlu.bisu.models.NewsResponse
import com.mrntlu.bisu.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines?apiKey=${Constants.API_KEY}&pageSize=${Constants.API_PAGE_SIZE}")
    suspend fun getBreakingNews(
        @Query("country") country: String,
        @Query("page") page: Int
    ): NewsResponse
}