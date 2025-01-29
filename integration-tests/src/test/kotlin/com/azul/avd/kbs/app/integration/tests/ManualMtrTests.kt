package com.azul.avd.kbs.app.integration.tests

import com.azul.avd.kbs.app.integration.steps.DataSteps
import com.azul.avd.kbs.app.integration.testutils.TestSpringContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
class ManualMtrTests: AbstractTest() {
    @Autowired
    private val applicationContext: ApplicationContext? = null

    @Autowired
    private lateinit var dataSteps: DataSteps

    @Test
    fun testGenerateMatrixForManualVulnerability() {
        printContext(logger, javaClass.toString(), applicationContext)
        dataSteps.processData( "ManualMtrTestsName1", dbName)
        dataSteps.processData( "ManualMtrTestsName2", dbName)
    }

    companion object {
        lateinit var dbName: String
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            dbName = "db_ManualMtrTests"
            TestSpringContext.changeMongoDbName(registry, dbName)
            TestSpringContext.changeSpringBatchDbName(registry, dbName)
        }
    }
}
