package dev.krylov.news.data

import dev.krylov.news.corp.Logger
import dev.krylov.news.data.models.Article
import dev.krylov.news.database.NewsDatabase
import dev.krylov.news.database.models.ArticleDB
import dev.krylov.newsapi.NewsApi
import dev.krylov.newsapi.modals.ArticleDT
import dev.krylov.newsapi.modals.ResponseDT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val logger: Logger,
) {
    fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {
        val cashArticles: Flow<RequestResult<List<Article>>> = getAllFromDB()
        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer(query)

        return cashArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    database.articleDao.observeAll().map { dbs -> dbs.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything(query = query)) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(result.getOrThrow().articles)
                }
            }
            .onEach { result ->
                if (result.isFailure) {
                    logger.e(
                        LOG_TAG,
                        "Error getting data from server. Cause = ${result.exceptionOrNull()}"
                    )
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDT<ArticleDT>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result: RequestResult<ResponseDT<ArticleDT>> ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDT>) {
        val dbs = data.map { articleDT -> articleDT.toArticleDB() }
        database.articleDao.insert(dbs)
    }

    private fun getAllFromDB(): Flow<RequestResult<List<Article>>> {
        val dbRequest = database.articleDao::getAll.asFlow()
            .map<List<ArticleDB>, RequestResult<List<ArticleDB>>> { RequestResult.Success(it) }
            .catch {
                logger.e(LOG_TAG, "Error getting from database. Cause = $it")
                emit(RequestResult.Error(error = it))
            }

        val start = flowOf<RequestResult<List<ArticleDB>>>(RequestResult.InProgress())

        return merge(start, dbRequest).map { result: RequestResult<List<ArticleDB>> ->
            result.map { articlesDBs ->
                articlesDBs.map { it.toArticle() }
            }
        }
    }

    private companion object {
        const val LOG_TAG = "ArticlesRepository"
    }
}
