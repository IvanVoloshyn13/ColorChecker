package com.example.foundation.model.sideeffects.navigator

import com.example.foundation.views.BaseScreen

/**
 * Side-effects interface for doing navigation from view-models.
 * You need to add [NavigatorPlugin] to your activity before using this feature.
 */
interface Navigator {

    /**
     * Launch a new screen at the top of back stack.
     */
    fun launch(screen: BaseScreen)

    /**
     * Go back to the previous screen and optionally send some results.
     */
    fun goBack(result: Any? = null)

}