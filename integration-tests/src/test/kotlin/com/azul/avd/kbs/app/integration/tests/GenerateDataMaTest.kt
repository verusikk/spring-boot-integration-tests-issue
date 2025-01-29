package com.azul.avd.kbs.app.integration.tests

import com.azul.avd.kbs.app.integration.steps.DataSteps
import com.azul.avd.kbs.app.integration.testutils.TestSpringContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
class GenerateDataMaTest : AbstractTest() {

    @Autowired
    lateinit var dataSteps: DataSteps

    @Autowired
    private val applicationContext: ApplicationContext? = null
    @Test
    fun test() {
        printContext(logger, javaClass.toString(), applicationContext)
        val name = "GenerateDataMaTestDataName"
        dataSteps.processData(name, dbName)
    }

    companion object {
        lateinit var dbName: String
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            dbName = "db_GenerateDataMaTest"
            TestSpringContext.changeSpringBatchDbName(registry, dbName)
            TestSpringContext.changeMongoDbName(registry, dbName)
        }
    }
}
