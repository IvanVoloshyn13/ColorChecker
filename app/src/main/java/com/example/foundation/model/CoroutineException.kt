package com.example.foundation.model

import kotlinx.coroutines.CancellableContinuation
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <T> CancellableContinuation<T>.toEmitter(): Emitter<T> {
    return object : Emitter<T> {
        var isDone = AtomicBoolean(false)
        override fun emit(finalResult: FinalResource<T>) {
            if (isDone.compareAndSet(false, true)) {
                when (finalResult) {
                    is SuccessResource -> resume(finalResult.data)
                    is ErrorResource -> resumeWithException(finalResult.exception)
                }
            }
        }

        override fun setCancelListener(cancelListener: CancelListener) {
            invokeOnCancellation { cancelListener() }
        }

    }
}