package com.example.changecolorsapp.views

import com.example.changecolorsapp.views.base.BaseScreen

/**
 * Navigation for your application
 */
interface Navigator {

    fun launch(screen: BaseScreen)

    /**
     * Go back to the previous screen and optionally send some results.
     */
    fun goBack(result: Any? = null)
}