package com.mrntlu.bisu.interfaces

interface Interaction<T> {
    fun onItemSelected(position: Int, item: T)

    fun onFavTogglePressed(position: Int, item: T, isFavAdded: Boolean)
}