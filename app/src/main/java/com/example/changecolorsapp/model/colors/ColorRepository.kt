package com.example.changecolorsapp.model.colors

import com.example.foundation.model.Repository


typealias ColorListener=(NamedColor)->Unit



interface ColorRepository: Repository {

    var currentColor: NamedColor

    /**
     * Get the list of all available colors hat may be chosen by the user
     */

    fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color by its Id
     */

    fun getById(id: Long): NamedColor

    /**
     * Listen for current color changes.
     * The listener is triggered immediately with the current value when calling this method
     */

    fun addListener(listener: ColorListener)

    /**
     * Stop listening for the current color changes
     */

    fun removeListener(listener: ColorListener)
}