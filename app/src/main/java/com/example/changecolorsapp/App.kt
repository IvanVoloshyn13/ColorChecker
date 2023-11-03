package com.example.changecolorsapp

import android.app.Application
import com.example.changecolorsapp.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes */

class App : Application() {
    val models = listOf<Any>(
        InMemoryColorsRepository()
    )
}