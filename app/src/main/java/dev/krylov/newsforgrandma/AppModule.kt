package dev.krylov.newsforgrandma

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.krylov.news.data.ArticlesRepository
import dev.krylov.news.database.NewsDatabase
import dev.krylov.newsapi.NewsApi
import javax.inject.Singleton
import dev.krylov.news.corp.AndroidLogcatLogger
import dev.krylov.news.corp.AppDispatchers
import dev.krylov.news.corp.Logger
import okhttp3.OkHttpClient
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            httpClient = okHttpClient,
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return NewsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    fun provideLogger(): Logger = AndroidLogcatLogger()

}