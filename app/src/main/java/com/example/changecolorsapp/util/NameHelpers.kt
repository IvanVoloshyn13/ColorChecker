package com.example.changecolorsapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>