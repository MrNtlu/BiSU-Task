package com.mrntlu.bisu.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.mrntlu.bisu.models.NewsPagingSource
import com.mrntlu.bisu.service.NewsApiService
import com.mrntlu.bisu.util.Constants
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService
) {

    fun getBreakingNews(
        country: String,
    ) = Pager(
        config = PagingConfig(
            pageSize = Constants.API_PAGE_SIZE,
        ),
        pagingSourceFactory = { NewsPagingSource(newsApiService, country) }
    ).liveData
}