package com.azul.avd.kbs.app.integration.testconfig.extensions

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler

/**
 * Catch exception on initialization and let test start and fail to avoid:
 * - Orphaned failures that don't disappear on rerun
 * - lost attachments
 */
class InitializationExceptionExtension: LifecycleMethodExecutionExceptionHandler, BeforeEachCallback {
    override fun handleBeforeAllMethodExecutionException(context: ExtensionContext, throwable: Throwable?) {
        getStore(context).put(Const.EXCEPTION, throwable)
        getStore(context).put(Const.CONTEXT, context)
    }

    override fun beforeEach(context: ExtensionContext) {
        val throwable = getStore(context).getOrDefault(
            Const.EXCEPTION,
            Throwable::class.java, null
        )
        if (throwable != null) {
            throw throwable
        }
    }

    private fun getStore(context: ExtensionContext): ExtensionContext.Store {
        return context.getStore(ExtensionContext.Namespace.create(javaClass))
    }

    object Const {
        const val EXCEPTION: String = "exception"
        const val CONTEXT: String = "context"
    }
}
