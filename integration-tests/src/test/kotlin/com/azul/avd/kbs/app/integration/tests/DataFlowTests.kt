package com.azul.avd.kbs.app.integration.tests

import com.azul.avd.kbs.app.integration.steps.DataSteps
import com.azul.avd.kbs.app.integration.testutils.TestSpringContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataFlowTests : AbstractTest() {

    @Autowired
    lateinit var dataSteps: DataSteps

    @Autowired
    private val applicationContext: ApplicationContext? = null

    @Test
    fun test1() {
        printContext(logger, javaClass.toString(), applicationContext)
        dataSteps.processData("DataFlowTestsName1", dbName)
    }

    @Test
    fun test2() {
        dataSteps.processData("DataFlowTestsName2", dbName)
    }

    companion object {
        lateinit var dbName: String
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            dbName = "app_DataFlowTests"
            TestSpringContext.changeMongoDbName(registry, dbName)
            TestSpringContext.changeSpringBatchDbName(registry, dbName)
        }
    }
}
