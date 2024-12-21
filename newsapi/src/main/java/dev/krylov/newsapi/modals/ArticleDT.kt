package dev.krylov.newsapi.modals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class ArticleDT (
    @SerialName("source") val source: SourceDT,
    @SerialName("author") val author: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("urlToImage") val urlToImage: String,
    @SerialName("publishedAt") val publishedAt: Date,
    @SerialName("content") val content: String
)