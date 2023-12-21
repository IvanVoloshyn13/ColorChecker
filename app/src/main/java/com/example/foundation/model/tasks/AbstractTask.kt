package com.example.foundation.model.tasks

import com.example.foundation.model.ErrorResource
import com.example.foundation.model.FinalResource
import com.example.foundation.model.SuccessResource
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.model.tasks.factories.TaskBody
import com.example.foundation.util.delegate.Await

abstract class AbstractTask<T> : Task<T> {

    private var finalResult by Await<FinalResource<T>>()

    final override fun await(): T {
        val wrapperListener: TaskListener<T> = {
            finalResult = it
        }
        doEnqueue(wrapperListener)
        try {
            when (val result = finalResult) {
                is ErrorResource -> throw result.exception
                is SuccessResource -> return result.data
            }
        } catch (e: Exception) {
            if (e is InterruptedException) {
                cancel()
                throw CancelledException(e)
            } else {
                throw e
            }
        }
    }

    final override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        val wrappedListener: TaskListener<T> = {
            finalResult = it
            dispatcher.dispatch {
                listener(finalResult)
            }
        }
        doEnqueue(wrappedListener)
    }

    final override fun cancel() {
        finalResult = ErrorResource(CancelledException())
        doCancel()
    }

    fun executeBody(taskBody: TaskBody<T>, listener: TaskListener<T>) {
        try {
            val data = taskBody()
            listener(SuccessResource(data))
        } catch (e: Exception) {
            listener(ErrorResource(e))
        }
    }

    /**
     * Launch the task asynchronously. Listener should be called when task is finished.
     * You may also use [executeBody] if your task executes [TaskBody] in some way.
     */
    abstract fun doEnqueue(listener: TaskListener<T>)

    /**
     * Cancel the task.
     */
    abstract fun doCancel()

}