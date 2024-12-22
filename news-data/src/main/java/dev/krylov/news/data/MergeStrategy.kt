package dev.krylov.news.data

interface MergeStrategy<E> {

    fun merge(cache: E, serverRequest: E): E
}

internal class RequestResponseMergeStrategy<T: Any>: MergeStrategy<RequestResult<T>> {
    override fun merge(cache: RequestResult<T>, serverRequest: RequestResult<T>): RequestResult<T> {
        return when {
            cache is RequestResult.InProgress && serverRequest is RequestResult.InProgress ->
                merge(cache, serverRequest)

            cache is RequestResult.Success && serverRequest is RequestResult.InProgress ->
                merge(cache, serverRequest)

            cache is RequestResult.InProgress && serverRequest is RequestResult.Success ->
                merge(cache, serverRequest)

            cache is RequestResult.Success && serverRequest is RequestResult.Error ->
                merge(cache, serverRequest)

            else -> error("Unimplemented branch")
        }
    }

    private fun merge(cache: RequestResult.InProgress<T>, serverRequest: RequestResult.InProgress<T>): RequestResult<T> {
        return when {
            serverRequest.data != null -> RequestResult.InProgress(serverRequest.data)
            else ->  RequestResult.InProgress(cache.data)
        }
    }

    private fun merge(cache: RequestResult.Success<T>, serverRequest: RequestResult.InProgress<T>): RequestResult<T> {
        return RequestResult.InProgress(cache.data)
    }

    private fun merge(cache: RequestResult.InProgress<T>, serverRequest: RequestResult.Success<T>): RequestResult<T> {
        return RequestResult.InProgress(serverRequest.data)
    }

    private fun merge(cache: RequestResult.Success<T>, serverRequest: RequestResult.Error<T>): RequestResult<T> {
        return RequestResult.Error(data = cache.data, error = serverRequest.error)
    }
}