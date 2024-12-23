package dev.krylov.newsapi.modals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDT (
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String
)
