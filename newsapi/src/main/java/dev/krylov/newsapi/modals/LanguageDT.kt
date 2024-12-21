package dev.krylov.newsapi.modals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LanguageDT {
    @SerialName("AR")
    AR,
    @SerialName("de")
    DE,
    @SerialName("en")
    EN,
    @SerialName("es")
    ES,
    @SerialName("fr")
    FR,
    @SerialName("he")
    HE,
    @SerialName("it")
    IT,
    @SerialName("nl")
    NL,
    @SerialName("no")
    NO,
    @SerialName("pt")
    PT,
    @SerialName("ru")
    RU,
    @SerialName("sv")
    SV,
    @SerialName("ud")
    UD,
    @SerialName("zh")
    ZH,
}