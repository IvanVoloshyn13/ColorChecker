package com.example.foundation.model.sideeffects.resources.plugin

import android.content.Context
import com.example.foundation.model.sideeffects.SideEffectMediator
import com.example.foundation.model.sideeffects.resources.Resources


class ResourcesSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Resources {

    override fun getString(resId: Int, vararg args: Any): String {
        return appContext.getString(resId, *args)
    }

}