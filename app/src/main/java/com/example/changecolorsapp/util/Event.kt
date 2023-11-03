package com.example.changecolorsapp.util
import androidx.lifecycle.LiveData
class Event<T>(
    private val value: T
) {
    private var handled = false
    fun getValue(): T? {
        if (handled) return null
        handled = true
        return value
    }
}