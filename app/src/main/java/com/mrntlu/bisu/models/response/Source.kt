package com.mrntlu.bisu.models.response

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Source(
    val id: String?,
    val name: String
) {
    //For Firebase
    constructor(): this(null, "")
}