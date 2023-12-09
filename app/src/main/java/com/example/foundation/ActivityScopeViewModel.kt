package com.example.foundation

import androidx.lifecycle.ViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions

const val ARG_SCREEN = "ARG_SCREEN"


class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(),
    Navigator by navigator,
    UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }


}