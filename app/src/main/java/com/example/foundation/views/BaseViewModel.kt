package com.example.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foundation.model.Resource
import com.example.foundation.util.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Resource<T>>
typealias MutableLiveResult<T> = MutableLiveData<Resource<T>>
typealias MediatorLiveResult<T> =  MediatorLiveData<Resource<T>>

/**
 * Base class for all view-models.
 */
open class BaseViewModel : ViewModel() {


    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {

    }
}