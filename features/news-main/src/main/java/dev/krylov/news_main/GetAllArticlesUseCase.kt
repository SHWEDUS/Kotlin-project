package dev.krylov.news_main

import dev.krylov.news.data.ArticlesRepository
import dev.krylov.news.data.RequestResult
import dev.krylov.news.data.models.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): RequestResult<Flow<List<Article>>> {
        return repository.getAll()

    }
}