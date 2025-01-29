package com.azul.avd.kbs.app.integration.tests

import com.azul.avd.kbs.app.integration.testextensions.AllExtensions
import org.slf4j.Logger
import org.springframework.context.ApplicationContext
import org.springframework.core.env.ConfigurableEnvironment

abstract class AbstractTest: AllExtensions {
    lateinit var logger: Logger

    fun configureLogger(logger: Logger) { // called throw reflection
        this.logger = logger
    }

    fun printContext(logger: Logger, javaClass: String, context: ApplicationContext?) {
        val prefix = "TEST DEBUG $javaClass"
        if (context != null) {
            val environment: ConfigurableEnvironment = context.environment as ConfigurableEnvironment
            environment.propertySources.forEach { propertySource ->
                logger.info("[$prefix] Property Source: ${propertySource.name}")
                if (propertySource is org.springframework.core.env.MapPropertySource) {
                    propertySource.source.forEach { (key, value) ->
                        logger.info("[$prefix] $key = $value")
                    }
                }
            }
            logger.info("[$prefix] spring.datasource.url=${context.environment.getProperty("spring.datasource.url")}")
            logger.info("[$prefix] spring.data.mongodb.database=${context.environment.getProperty("spring.data.mongodb.database")}")
        }
    }
}