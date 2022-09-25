package com.mrntlu.bisu.models.response

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?
) {
    //For Firebase
    constructor(): this(null, null, null, "", Source(), "", "", "")

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "author" to author,
            "title" to title,
            "description" to description,
            "publishedAt" to publishedAt,
            "source" to source,
            "title" to title,
            "url" to url,
            "urlToImage" to urlToImage,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other is Article) {
            return other.url == url
        }
        return false
    }

    override fun hashCode(): Int {
        var result = author?.hashCode() ?: 0
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (urlToImage?.hashCode() ?: 0)
        return result
    }
}