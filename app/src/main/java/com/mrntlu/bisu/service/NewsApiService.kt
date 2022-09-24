package com.mrntlu.bisu.service

import com.mrntlu.bisu.models.NewsResponse
import com.mrntlu.bisu.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines?apiKey=${Constants.API_KEY}&pageSize=${Constants.API_PAGE_SIZE}")
    suspend fun getBreakingNews(
        @Query("country") country: String,
        @Query("page") page: Int
    ): Response<NewsResponse>

    //TODO: Remove
    //https://newsapi.org/v2/top-headlines?country=us&apiKey=df7b2e0f74e44f958d17988a0d26ac2e&pageSize=10&page=1
}