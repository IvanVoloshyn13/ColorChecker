package com.example.foundation.model.sideeffects.permissions.plugin

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.foundation.model.ErrorResource
import com.example.foundation.model.sideeffects.SideEffectMediator
import com.example.foundation.model.sideeffects.permissions.Permissions

import com.example.foundation.model.Emitter
import com.example.foundation.model.toEmitter
import kotlinx.coroutines.suspendCancellableCoroutine


class PermissionsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<PermissionsSideEffectImpl>(), Permissions {

    val retainedState = RetainedState()

    override fun hasPermissions(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestPermission(permission: String): PermissionStatus = suspendCancellableCoroutine  { continuation->
      val emitter=continuation.toEmitter()
        if (retainedState.emitter != null) {
            emitter.emit(ErrorResource(IllegalStateException("Only one permission request can be active")))
            return@suspendCancellableCoroutine
        }
        retainedState.emitter = emitter
        target { implementation ->
            implementation.requestPermission(permission)
        }
    }

    class RetainedState(
        var emitter: Emitter<PermissionStatus>? = null
    )

}