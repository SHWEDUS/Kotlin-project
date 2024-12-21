package dev.krylov.newsapi.modals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 * "status": "ok",
 * "totalResults": 1654,
 * +"articles": [ … ]
 *
 */
@Serializable
data class Response<E>(
    @SerialName("status")
    val status: String,

    @SerialName("totalResult")
    val totalResult: Int,

    @SerialName("articles")
    val articles:List<E>)