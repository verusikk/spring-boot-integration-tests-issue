package com.azul.avd.kbs.app.integration.testutils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule

val mapper: ObjectMapper = jacksonObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS , false)
    .registerModule(kotlinModule()) // kotlin module to work with data classes
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun <T> T.toJsonString(): String {
    return mapper.writeValueAsString(this)
}
