package com.mrntlu.bisu.viewmodel

import androidx.lifecycle.ViewModel
import com.mrntlu.bisu.repository.FavouritesRepository
import androidx.lifecycle.LifecycleOwner
import com.mrntlu.bisu.models.response.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: FavouritesRepository
): ViewModel() {

    fun setAnonymousUser() = repository.setAnonymousUser()

    fun addNewFavouriteNews(article: Article) = repository.addNewFavouriteNews(article)

    fun deleteFavouriteNews(url: String) = repository.deleteFavouriteNews(url)

    fun setFavsCountListener() = repository.setFavsCountListener()

    fun removeListeners(owner: LifecycleOwner) = repository.removeListeners(owner)

    fun getAllFavouritesAndSetListener() = repository.getAllFavouritesAndSetListener()

    fun onDestroy() = repository.onDestroy()
}