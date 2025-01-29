package com.azul.avd.kbs.app.integration.testutils

import org.springframework.test.context.DynamicPropertyRegistry

object TestSpringContext {

    fun changeSpringBatchDbName(registry: DynamicPropertyRegistry, dbName: String) {
        val dbUrl = "jdbc:h2:mem:testdb-$dbName;DB_CLOSE_DELAY=-1"
        registry.add("spring.datasource.url", dbUrl::toString)
    }

    fun changeMongoDbName(registry: DynamicPropertyRegistry, dbName: String) {
        registry.add("spring.data.mongodb.database", dbName::toString)
    }
}
