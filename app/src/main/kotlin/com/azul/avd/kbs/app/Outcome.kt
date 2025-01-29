package com.azul.avd.kbs.app

import org.slf4j.Logger

/**
 * A type-safe version of stdlib [Result]
 */
sealed class Outcome<out T>

data class Ok<out T>(val value: T) : Outcome<T>()

data class Er(val error: Throwable) : Outcome<Nothing>()

fun <T> Outcome<T>.assertOkOrFail(message: String, logger: Logger? = null): T {
    return when (this) {
        is Ok -> {
            this.value
        }

        is Er -> {
            logger?.error(message, error)
            error(message)
        }
    }
}
