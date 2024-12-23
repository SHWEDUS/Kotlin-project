package dev.krylov.news.data

import dev.krylov.news.data.models.Article
import dev.krylov.news.database.models.ArticleDB
import dev.krylov.newsapi.modals.ArticleDT
import dev.krylov.news.data.models.Source
import dev.krylov.newsapi.modals.SourceDT
import dev.krylov.news.database.models.Source as SourceDB

internal fun ArticleDB.toArticle(): Article {
    return Article(
        cacheId = id,
        source = source.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}

internal fun SourceDB.toSource(): Source {
    return Source(id = id, name = name)
}

internal fun SourceDT.toSource(): Source {
    return Source(id = id ?: name, name = name)
}

internal fun SourceDT.toSourceDB(): SourceDB {
    return SourceDB(id = id ?: name, name = name)
}

internal fun ArticleDT.toArticle() : Article {
    return Article(
        source = source.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}

internal fun ArticleDT.toArticleDB(): ArticleDB {
    return ArticleDB(
        source = source.toSourceDB(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}