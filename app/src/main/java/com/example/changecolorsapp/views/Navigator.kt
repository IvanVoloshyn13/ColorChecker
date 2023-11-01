package com.example.changecolorsapp.views

/**
 * Navigation for your application
 */
interface Navigator {

    fun launch(screen:BaseScreen)

    /**
     * Go back to the previous screen and optionally send some results.
     */
    fun goBack(result: Any? = null)
}