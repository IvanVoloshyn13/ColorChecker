package com.example.foundation.model

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Resource<T> {

    fun <R> map(mapper: Mapper<T, R>? = null): Resource<R> {
        return when (this) {
            is LoadingResource -> LoadingResource()
            is SuccessResource -> {
                if (mapper == null) throw IllegalArgumentException("Mapper should not be Null for success result")
                SuccessResource(mapper(this.data))
            }

            is ErrorResource -> ErrorResource(this.exception)
        }
    }
}

/**
 * Operation has been finished
 */
sealed class FinalResource<T> : Resource<T>()

class LoadingResource<T> : Resource<T>()
class SuccessResource<T>(val data: T) : FinalResource<T>()
class ErrorResource<T>(val exception: Exception) : FinalResource<T>()

fun <T> Resource<T>?.takeSuccess(): T? {
    return if (this is SuccessResource) {
        this.data
    } else null
}