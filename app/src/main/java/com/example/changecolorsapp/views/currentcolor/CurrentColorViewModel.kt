package com.example.changecolorsapp.views.currentcolor


import android.Manifest
import com.example.changecolorsapp.R
import com.example.changecolorsapp.model.colors.ColorListener
import com.example.changecolorsapp.model.colors.ColorsRepository
import com.example.changecolorsapp.model.colors.NamedColor
import com.example.changecolorsapp.views.changecolor.ChangeColorFragment
import com.example.foundation.model.LoadingResource
import com.example.foundation.model.SuccessResource
import com.example.foundation.model.sideeffects.dialogs.Dialogs
import com.example.foundation.model.sideeffects.dialogs.plugin.DialogConfig
import com.example.foundation.model.sideeffects.intents.Intents
import com.example.foundation.model.sideeffects.navigator.Navigator
import com.example.foundation.model.sideeffects.permissions.Permissions
import com.example.foundation.model.sideeffects.permissions.plugin.PermissionStatus
import com.example.foundation.model.sideeffects.resources.Resources
import com.example.foundation.model.sideeffects.toasts.Toasts
import com.example.foundation.model.takeSuccess
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MutableLiveResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val permissions: Permissions,
    private val intents: Intents,
    private val dialogs: Dialogs,
    private val colorsRepository: ColorsRepository,

    ) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(LoadingResource())
    val currentColor: LiveResult<NamedColor> = _currentColor



    // --- example of listening results via model layer

    init {
        viewModelScope.launch {
            colorsRepository.listenCurrentColor().collectLatest {
                _currentColor.postValue(SuccessResource(it))
            }
        }

        load()
    }


    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    /**
     * Example of using side-effect plugins
     */
    fun requestPermission() = viewModelScope.launch {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = permissions.hasPermissions(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog())
        } else {
            when (permissions.requestPermission(permission)) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_grated))
                }

                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }

                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAskForLaunchingAppSettingsDialog())) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }

    fun tryAgain() {
        load()
    }

    private fun load() = into(_currentColor) {
        return@into colorsRepository.getCurrentColor()
    }

    private fun createPermissionAlreadyGrantedDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.permissions_already_granted),
        positiveButton = resources.getString(R.string.action_ok)
    )

    private fun createAskForLaunchingAppSettingsDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.open_app_settings_message),
        positiveButton = resources.getString(R.string.action_open),
        negativeButton = resources.getString(R.string.action_cancel)
    )
}