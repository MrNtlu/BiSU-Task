package com.mrntlu.bisu.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {
    fun getBreakingNews(
        country: String = "us",
    ): LiveData<PagingData<Article>> =repository.getBreakingNews(country).cachedIn(viewModelScope)
}