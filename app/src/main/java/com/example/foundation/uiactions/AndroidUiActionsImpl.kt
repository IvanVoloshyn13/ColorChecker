package com.example.foundation.uiactions

import android.app.Application
import android.content.Context
import android.widget.Toast

class AndroidUiActionsImpl(private val appContext: Context) : UiActions {

    override fun toast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun getString(messageRes: Int, vararg args: Any): String {
        return appContext.getString(messageRes, *args)
    }
}