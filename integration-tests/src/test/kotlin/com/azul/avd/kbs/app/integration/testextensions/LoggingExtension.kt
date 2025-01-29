package com.azul.avd.kbs.app.integration.testextensions

import com.azul.avd.kbs.app.integration.testconfig.ThreadNameBasedDiscriminator
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import org.junit.jupiter.api.extension.TestWatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggingExtension: TestWatcher, BeforeEachCallback, TestInstancePostProcessor {
    private var logger: Logger? = null

    override fun testSuccessful(context: ExtensionContext) {
        logger!!.info("[TEST-END] " + context.displayName)
    }

    override fun testFailed(context: ExtensionContext, cause: Throwable?) {
        logger!!.error("[TEST-FAILURE] " + context.displayName, cause)
        logger!!.error("[TEST-END] " + context.displayName)
    }

    @Throws(Exception::class)
    override fun beforeEach(context: ExtensionContext) {
        logger!!.info("[TEST-START] " + context.displayName)
    }

    @Throws(Exception::class)
    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext?) {
        // set current thread name to a test class name to use in log record and make it more informative
        Thread.currentThread().name = testInstance.javaClass.simpleName
        // set log file name
        ThreadNameBasedDiscriminator.setLogFileName(testInstance.javaClass.canonicalName)
        logger = LoggerFactory.getLogger(testInstance.javaClass)
        testInstance.javaClass
            .getMethod("configureLogger", Logger::class.java)
            .invoke(testInstance, logger)
    }
}