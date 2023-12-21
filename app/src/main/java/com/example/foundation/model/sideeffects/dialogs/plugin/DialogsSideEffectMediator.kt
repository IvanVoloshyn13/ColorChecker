package com.example.foundation.model.sideeffects.dialogs.plugin

import com.example.foundation.model.Emitter
import com.example.foundation.model.ErrorResource
import com.example.foundation.model.sideeffects.SideEffectMediator
import com.example.foundation.model.sideeffects.dialogs.Dialogs
import com.example.foundation.model.toEmitter
import kotlinx.coroutines.suspendCancellableCoroutine


class DialogsSideEffectMediator : SideEffectMediator<DialogsSideEffectImpl>(), Dialogs {

    var retainedState = RetainedState()

    override suspend fun show(dialogConfig: DialogConfig): Boolean =
        suspendCancellableCoroutine { continuation ->
            val emitter = continuation.toEmitter()
            if (retainedState.record != null) {
                // for now allowing only 1 active dialog at a time
                emitter.emit(ErrorResource(IllegalStateException("Can't launch more than 1 dialog at a time")))
                return@suspendCancellableCoroutine
            }

            val wrappedEmitter = Emitter.wrap(emitter) {
                retainedState.record = null
            }

            val record = DialogRecord(wrappedEmitter, dialogConfig)
            wrappedEmitter.setCancelListener {
                target { implementation ->
                    implementation.removeDialog()
                }
            }

            target { implementation ->
                implementation.showDialog(record)
            }

            retainedState.record = record
        }

    class DialogRecord(
        val emitter: Emitter<Boolean>,
        val config: DialogConfig
    )

    class RetainedState(
        var record: DialogRecord? = null
    )
}