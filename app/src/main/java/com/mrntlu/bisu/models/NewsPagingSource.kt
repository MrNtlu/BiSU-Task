package com.mrntlu.bisu.models

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.service.NewsApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val newsApiService: NewsApiService,
    private val query: String,
): PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1

        return try {
            val response = newsApiService.getBreakingNews(query, page)

            //TODO: To test and showcase
            //TODO: REMOVE!
            delay(1200L)

            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.articles.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}