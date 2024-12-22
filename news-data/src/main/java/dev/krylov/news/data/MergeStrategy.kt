package dev.krylov.news.data

import dev.krylov.news.data.RequestResult.Error
import dev.krylov.news.data.RequestResult.InProgress
import dev.krylov.news.data.RequestResult.Success

interface MergeStrategy<E> {

    fun merge(cache: E, serverRequest: E): E
}

internal class RequestResponseMergeStrategy<T: Any>: MergeStrategy<RequestResult<T>> {
    override fun merge(cache: RequestResult<T>, serverRequest: RequestResult<T>): RequestResult<T> {
        return when {
            cache is InProgress && serverRequest is InProgress -> merge(cache, serverRequest)
            cache is Success && serverRequest is InProgress -> merge(cache, serverRequest)
            cache is InProgress && serverRequest is Success -> merge(cache, serverRequest)
            cache is Success && serverRequest is Error -> merge(cache, serverRequest)
            cache is Success && serverRequest is Success -> merge(cache, serverRequest)

            else -> error("Unimplemented branch")
        }
    }

    private fun merge(cache: InProgress<T>, serverRequest: InProgress<T>): RequestResult<T> {
        return when {
            serverRequest.data != null -> InProgress(serverRequest.data)
            else ->  InProgress(cache.data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: Success<T>, serverRequest: InProgress<T>): RequestResult<T> {
        return InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: InProgress<T>, serverRequest: Success<T>): RequestResult<T> {
        return InProgress(serverRequest.data)
    }

    private fun merge(cache: Success<T>, serverRequest: Error<T>): RequestResult<T> {
        return Error(data = cache.data, error = serverRequest.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(cache: Success<T>, serverRequest: Success<T>): RequestResult<T> {
        return Success(data = serverRequest.data)
    }
}