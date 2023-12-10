package com.example.changecolorsapp.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.changecolorsapp.R
import com.example.changecolorsapp.model.colors.ColorListener
import com.example.changecolorsapp.model.colors.ColorRepository

import com.example.changecolorsapp.model.colors.NamedColor
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.changecolorsapp.views.changecolor.ChangeColorFragment
import com.example.foundation.model.LoadingResource
import com.example.foundation.model.Resource
import com.example.foundation.model.SuccessResource
import com.example.foundation.model.takeSuccess
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MutableLiveResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorRepository
) : BaseViewModel() {
    private val _currentColor = MutableLiveResult<NamedColor>(LoadingResource())
    val currentColor: LiveResult<NamedColor> = _currentColor


    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResource(it))
    }

    init {
        viewModelScope.launch {
            delay(2000)
            colorsRepository.addListener(colorListener)
        }

    }



    // --- example of listening results via model layer


    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}