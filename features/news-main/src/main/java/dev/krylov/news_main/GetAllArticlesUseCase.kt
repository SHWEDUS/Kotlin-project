package dev.krylov.news_main

import dev.krylov.news.data.ArticlesRepository
import dev.krylov.news.data.RequestResult
import dev.krylov.news.data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.krylov.news.data.models.Article as DataArticle

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<Article>>> {
        return repository.getAll()
            .map { requestResult -> requestResult.map {
                    articles ->
                articles.map { it.toUiArticles() }
            } }

    }
}


private fun DataArticle.toUiArticles(): Article {
    TODO("Not implemented")
}