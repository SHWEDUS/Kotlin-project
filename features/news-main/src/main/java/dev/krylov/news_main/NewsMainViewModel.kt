package dev.krylov.news_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krylov.news.data.ArticlesRepository
import dev.krylov.news.data.RequestResult
import dev.krylov.news.data.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor (
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
): ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
        .map {it.toState()}
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}

private fun RequestResult<List<Article>>.toState(): State {
    return when(this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}


sealed class State {

    data object None: State()
    class Loading(val articles: List<Article>? = null): State()
    class Error(val articles: List<Article>? = null): State()
    class Success(val articles: List<Article>?): State()
}