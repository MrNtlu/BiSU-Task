package com.mrntlu.bisu.interfaces

interface Interaction<T> {
    fun onItemSelected(position: Int, item: T)

    fun onErrorRefreshPressed()
}