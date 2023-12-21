package com.example.changecolorsapp

import android.app.Application
import com.example.changecolorsapp.model.colors.InMemoryColorsRepository
import com.example.foundation.BaseApplication
import com.example.foundation.model.coroutines.IoDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * Here we store instances of model layer classes.
 */
class App : Application(), BaseApplication {

    private val ioDispatcher = IoDispatcher(Dispatchers.IO)

    /**
     * Place your singleton scope dependencies here
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        InMemoryColorsRepository(ioDispatcher)
    )

}