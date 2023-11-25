package com.example.changecolorsapp.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.changecolorsapp.R
import com.example.changecolorsapp.model.colors.ColorListener
import com.example.changecolorsapp.model.colors.ColorRepository

import com.example.changecolorsapp.model.colors.NamedColor
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.changecolorsapp.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorRepository
) : BaseViewModel() {
    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor


    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    init {
        colorsRepository.addListener(colorListener)
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
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}