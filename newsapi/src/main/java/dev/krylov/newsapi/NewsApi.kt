package dev.krylov.newsapi

import SortBy
import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dev.krylov.newsapi.modals.ArticleDT
import dev.krylov.newsapi.modals.LanguageDT
import dev.krylov.newsapi.modals.ResponseDT
import dev.krylov.newsapi.utils.NewsApiKeyInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface NewsApi {
    @GET("everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<@JvmSuppressWildcards LanguageDT>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<ResponseDT<ArticleDT>>
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    httpClient: OkHttpClient? = null,
    json: Json = Json,
): NewsApi {
    val retrofit = retrofit(baseUrl, apiKey, httpClient, json)
    return retrofit.create()
}

private fun retrofit(
    baseUrl: String,
    apiKey: String,
    httpClient: OkHttpClient?,
    json: Json,
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val modifiedHttpClient = (httpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(NewsApiKeyInterceptor(apiKey)).build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedHttpClient)
        .build()
}
