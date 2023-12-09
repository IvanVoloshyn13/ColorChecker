package com.example.changecolorsapp

import android.app.Application
import com.example.changecolorsapp.model.colors.InMemoryColorsRepository
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository

/**
 * Here we store instances of model layer classes */

class App : Application(), BaseApplication {

    val models = listOf<Repository>(
        InMemoryColorsRepository()
    )
    override val repositories: List<Repository>
        get() = models
}