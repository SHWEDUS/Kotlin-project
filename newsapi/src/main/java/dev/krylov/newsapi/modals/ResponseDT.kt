package dev.krylov.newsapi.modals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseDT<E>(
    @SerialName("status") val status: String,

    @SerialName("totalResult") val totalResult: Int,

    @SerialName("articles") val articles:List<E>)