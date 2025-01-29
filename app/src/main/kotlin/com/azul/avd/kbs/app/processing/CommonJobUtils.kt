package com.azul.avd.kbs.app.processing

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder

internal const val DATA_NAME = "dataName"

fun JobParametersBuilder.addDataName(dataName: String) {
    require(dataName.isNotBlank())
    addString(DATA_NAME, dataName)
}

internal fun Map<String, Any>.getDataName(): String {
    return this[DATA_NAME] as String
}

fun JobParameters.getDataName(): String {
    return getString(DATA_NAME) ?: error("Data Name must not be null")
}