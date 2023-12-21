package com.example.foundation.model.sideeffects.permissions.plugin

import android.content.Context
import com.example.foundation.model.sideeffects.SideEffectMediator
import com.example.foundation.model.sideeffects.SideEffectPlugin


/**
 * Plugin for managing permissions from view-models.
 * This plugin allows using [Permissions] interface to view-model constructor.
 */
class PermissionsPlugin :
    SideEffectPlugin<PermissionsSideEffectMediator, PermissionsSideEffectImpl> {

    override val mediatorClass: Class<PermissionsSideEffectMediator>
        get() = PermissionsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<PermissionsSideEffectImpl> {
        return PermissionsSideEffectMediator(applicationContext)
    }

    override fun createImplementation(mediator: PermissionsSideEffectMediator): PermissionsSideEffectImpl {
        return PermissionsSideEffectImpl(mediator.retainedState)
    }

}