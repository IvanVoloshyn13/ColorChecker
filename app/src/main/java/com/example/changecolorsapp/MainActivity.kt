package com.example.changecolorsapp

import android.os.Bundle
import com.example.changecolorsapp.views.currentcolor.CurrentColorFragment
import com.example.foundation.model.sideeffects.SideEffectPluginsManager
import com.example.foundation.model.sideeffects.dialogs.plugin.DialogsPlugin
import com.example.foundation.model.sideeffects.intents.plugin.IntentsPlugin
import com.example.foundation.model.sideeffects.navigator.plugin.NavigatorPlugin
import com.example.foundation.model.sideeffects.navigator.plugin.StackFragmentNavigator
import com.example.foundation.model.sideeffects.permissions.plugin.PermissionsPlugin
import com.example.foundation.model.sideeffects.resources.plugin.ResourcesPlugin
import com.example.foundation.model.sideeffects.toasts.plugin.ToastsPlugin
import com.example.foundation.views.activity.BaseActivity

/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens.
 */
class MainActivity : BaseActivity() {

    override fun registerPlugins(manager: SideEffectPluginsManager) = with (manager) {
        val navigator = createNavigator()
        register(ToastsPlugin())
        register(ResourcesPlugin())
        register(NavigatorPlugin(navigator))
        register(PermissionsPlugin())
        register(DialogsPlugin())
        register(IntentsPlugin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun createNavigator() = StackFragmentNavigator(
        containerId = R.id.fragmentContainer,
        defaultTitle = getString(R.string.app_name),
        animations = StackFragmentNavigator.Animations(
            enterAnim = R.anim.enter,
            exitAnim = R.anim.exit,
            popEnterAnim = R.anim.pop_enter,
            popExitAnim = R.anim.pop_exit
        ),
        initialScreenCreator = { CurrentColorFragment.Screen() }
    )

}