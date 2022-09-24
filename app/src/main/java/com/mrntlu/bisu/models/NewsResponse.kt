package com.mrntlu.bisu.models

import com.mrntlu.bisu.models.response.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)