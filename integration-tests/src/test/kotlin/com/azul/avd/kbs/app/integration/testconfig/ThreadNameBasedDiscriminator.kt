package com.azul.avd.kbs.app.integration.testconfig

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.sift.Discriminator

class ThreadNameBasedDiscriminator: Discriminator<ILoggingEvent> {

    private var started = false

    override fun getDiscriminatingValue(iLoggingEvent: ILoggingEvent?): String? {
        return if (logFileName.get() == null) {
            Thread.currentThread().name
        } else {
            logFileName.get()
        }
    }

    override fun getKey(): String {
        return KEY
    }

    override fun isStarted(): Boolean {
        return started
    }

    override fun start() {
        started = true
    }

    override fun stop() {
        started = false
    }

    companion object {
        @JvmStatic
        /**
         * Key for using in logback.xml to replace by discriminating value
         */
        val KEY: String = "logFileName"

        @JvmStatic
        fun getLogFileName(): String? {
            return logFileName.get()
        }
        @JvmStatic
        /**
         * logFileName for the current thread and all started from the current threads
         * thread is executed for a single test at the moment with junit parallelization
         * and logFileName will be set to a test class name by test
         */
        val logFileName: InheritableThreadLocal<String?> = InheritableThreadLocal()

        @JvmStatic
        /**
         * Set logFileName for logback to generate proper log file name
         * @param name name to set, supposed to be a test name
         */
        open fun setLogFileName(name: String?) {
            logFileName.set(name)
        }
    }
}