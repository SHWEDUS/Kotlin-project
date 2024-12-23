package dev.krylov.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import dev.krylov.news.NewsTheme

@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    if (state != State.None) {
        NewsMainContent(currentState)
    }
}



@Composable
private fun NewsMainContent(currentState: State) {
    Column {
        if (currentState is State.Error) {
            ErrorMessage(currentState)
        }
        if (currentState is State.Loading) {
            ProgressIndicator(currentState)
        }
        if (currentState.articles != null) {
            Articles(articles = currentState.articles)
        }
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ошибка обновления данных",
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp
        )
    }
}

@Composable
private fun Articles(
    @PreviewParameter(ArticlesPreviewProvider::class, limit = 1) articles: List<ArticleUI>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() // Учет выреза экрана
            .navigationBarsPadding()
    ) {
        items(articles) { article ->
            key(article.id) {
                Article(article)
            }
        }
    }
}

@Composable
internal fun Article(article: ArticleUI, modifier: Modifier = Modifier) {
    Card(
        modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        // Контент карточки (изображение и текст)
        Column {
            article.imageUrl?.let { imageUrl ->
                var isImageVisible by remember { mutableStateOf(true) }
                if (isImageVisible) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.content_description_article_image),
                        onState = { state ->
                            if (state is AsyncImagePainter.State.Error) {
                                isImageVisible = false
                            }
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = article.title ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = article.description ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3
                )
            }
        }
    }
}

@Suppress("Debug")
private class ArticlePreviewProvider : PreviewParameterProvider<ArticleUI> {
    override val values = sequenceOf(
        ArticleUI(
            1,
            "Android Studio Iguana is Stable!",
            "New stable version on Android IDE has been release",
            imageUrl = null,
            url = "",
        ),
        ArticleUI(
            2,
            "Gemini 1.5 Release",
            "Upgraded version of Google AI is available",
            imageUrl = null,
            url = "",
        ),
        ArticleUI(
            3,
            "Shape animations (10 min)",
            "How to use shape transform animations in Compose",
            imageUrl = null,
            url = "",
        ),
    )
}

private class ArticlesPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {
    private val articleProvider = ArticlePreviewProvider()
    override val values = sequenceOf(
        articleProvider.values
            .toList()
    )
}