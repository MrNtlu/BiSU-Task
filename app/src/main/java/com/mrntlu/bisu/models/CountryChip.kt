package com.mrntlu.bisu.models

import com.mrntlu.bisu.util.Constants

data class CountryChip(
    val code: String,
) {
    val flag
    get() = "${Constants.FLAG_URL}$code.png"
}