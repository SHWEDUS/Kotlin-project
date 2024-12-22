package dev.krylov.newsapi

import SortBy
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date
import androidx.annotation.IntRange
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dev.krylov.newsapi.modals.ArticleDT
import dev.krylov.newsapi.modals.LanguageDT
import dev.krylov.newsapi.modals.ResponseDT
import dev.krylov.newsapi.utils.NewsApiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

interface NewsApi {
    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("form") date: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") language: List<LanguageDT>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<ResponseDT<ArticleDT>>
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    httpClient: OkHttpClient? = null
): NewsApi {
    val retrofit = retrofit(baseUrl, apiKey, httpClient)
    return retrofit.create()
}

private fun retrofit (
    baseUrl: String,
    apiKey: String,
    httpClient: OkHttpClient?
): Retrofit {
    val modifiedHttpClient = (httpClient?.newBuilder()?: OkHttpClient.Builder())
        .addInterceptor(NewsApiKeyInterceptor(apiKey)).build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedHttpClient)
        .build()
}
