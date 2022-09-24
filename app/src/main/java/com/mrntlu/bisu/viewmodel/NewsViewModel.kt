package com.mrntlu.bisu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.bisu.models.NewsResponse
import com.mrntlu.bisu.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _newsResponse = MutableLiveData<NewsResponse>()
    val newsResponse: LiveData<NewsResponse>
        get() = _newsResponse

    init {
        getBreakingNews()
    }

    private fun getBreakingNews(
        country: String = "us",
        page: Int = 1,
    ) = viewModelScope.launch {

        repository.getBreakingNews(country, page).let {
            if (it.isSuccessful) {
                _newsResponse.postValue(it.body())
            } else {
                // TODO Error Handle
            }
        }
    }
}