package com.example.foundation.model.sideeffects.toasts.plugin

import android.content.Context
import android.widget.Toast
import com.example.foundation.model.sideeffects.SideEffectMediator
import com.example.foundation.model.sideeffects.toasts.Toasts
import com.example.foundation.model.tasks.dispatchers.MainThreadDispatcher


/**
 * Android implementation of [Toasts]. Displaying simple toast message and getting string from resources.
 */
class ToastsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Toasts {

    private val dispatcher = MainThreadDispatcher()

    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}