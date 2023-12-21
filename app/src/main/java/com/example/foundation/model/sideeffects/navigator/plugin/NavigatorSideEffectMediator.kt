package com.example.foundation.model.sideeffects.navigator.plugin

import com.example.foundation.model.sideeffects.SideEffectMediator
import com.example.foundation.model.sideeffects.navigator.Navigator
import com.example.foundation.views.BaseScreen

class NavigatorSideEffectMediator : SideEffectMediator<Navigator>(), Navigator {

    override fun launch(screen: BaseScreen) = target {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {
        it.goBack(result)
    }

}