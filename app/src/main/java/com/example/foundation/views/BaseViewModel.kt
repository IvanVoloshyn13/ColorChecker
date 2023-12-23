package com.example.foundation.views


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.foundation.model.ErrorResource
import com.example.foundation.model.Resource
import com.example.foundation.model.SuccessResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


typealias LiveResult<T> = LiveData<Resource<T>>
typealias MutableLiveResult<T> = MutableLiveData<Resource<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Resource<T>>

/**
 * Base class for all view-models.
 */
open class BaseViewModel(
) : ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate
    protected val viewModelScope: CoroutineScope = CoroutineScope(coroutineContext)


    override fun onCleared() {
        super.onCleared()
        clearViewModelScope()
    }

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {

    }

    /**
     * Override this method in child classes if you want to control go-back behaviour.
     * Return `true` if you want to abort closing this screen
     */
    open fun onBackPressed(): Boolean {
        clearViewModelScope()
        return false
    }


    /**
     * Launch task asynchronously and map its result to the specified
     * [liveResult].
     * Task is cancelled automatically if view-model is going to be destroyed.
     */
    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResource(block()))
            } catch (e: Exception) {
                liveResult.postValue(ErrorResource(e))
            }
        }

    }


    fun <T> into(stateFlow: MutableStateFlow<Resource<T>>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                stateFlow.value=SuccessResource(block())
            } catch (e: Exception) {
                stateFlow.value= ErrorResource(e)
            }
        }

    }
    private fun clearViewModelScope() {
        viewModelScope.cancel()
    }

    fun <T> SavedStateHandle.getStateFlowFromSavedState(key: String, initialValue: T): MutableStateFlow<T> {
        val savedStateHandle = this
        val mutableFlow = MutableStateFlow(savedStateHandle[key] ?: initialValue)
        viewModelScope.launch {
            mutableFlow.collect {
                savedStateHandle[key] = it
            }
        }

        return mutableFlow
    }

}