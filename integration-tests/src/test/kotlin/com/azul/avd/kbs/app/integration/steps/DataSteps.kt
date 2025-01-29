package com.azul.avd.kbs.app.integration.steps

import io.qameta.allure.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataSteps {

    @Autowired
    lateinit var jobSteps: DataJobSteps

    @Step("Process data")
    fun processData(name: String, dbName: String) {
        val execution = jobSteps.runDataProcessJob(name, dbName)
        jobSteps.waitJobCompleted(execution)
        jobSteps.attachJobAndSteps(execution)
    }
}
