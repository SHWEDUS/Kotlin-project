package dev.krylov.news_main

import dev.krylov.news.data.ArticlesRepository
import dev.krylov.news.data.RequestResult
import dev.krylov.news.data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.krylov.news.data.models.Article as DataArticle
import javax.inject.Inject

internal class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository,
) {

    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query)
            .map { requestResult -> requestResult.map {
                    articles ->
                articles.map { it.toUiArticle() }
            } }

    }
}


private fun DataArticle.toUiArticle(): ArticleUI {
    return ArticleUI(
        id = cacheId,
        title = title,
        description = description,
        imageUrl = urlToImage,
        url = url
    )
}