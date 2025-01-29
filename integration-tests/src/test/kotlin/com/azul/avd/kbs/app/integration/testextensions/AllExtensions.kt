package com.azul.avd.kbs.app.integration.testextensions


import com.azul.avd.kbs.app.integration.testconfig.extensions.InitializationExceptionExtension
import org.junit.jupiter.api.extension.RegisterExtension

interface AllExtensions {
    companion object {

        @JvmField
        @RegisterExtension
        val initializationExceptionExtension = InitializationExceptionExtension()

        @JvmField
        @RegisterExtension
        val loggingExtension = LoggingExtension()
    }
}
