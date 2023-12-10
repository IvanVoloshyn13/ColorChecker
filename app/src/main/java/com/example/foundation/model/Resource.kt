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

class LoadingResource<T> : Resource<T>()
class SuccessResource<T>(val data: T) : Resource<T>()
class ErrorResource<T>(val exception: Exception) : Resource<T>()

fun <T> Resource<T>?.takeSuccess(): T? {
    return if (this is SuccessResource) {
        this.data
    } else null
}