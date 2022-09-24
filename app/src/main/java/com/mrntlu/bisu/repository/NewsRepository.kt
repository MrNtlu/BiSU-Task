package com.mrntlu.bisu.repository

import com.mrntlu.bisu.service.NewsApiService
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService
) {

    suspend fun getBreakingNews(
        country: String,
        page: Int,
    ) = newsApiService.getBreakingNews(country, page)
}