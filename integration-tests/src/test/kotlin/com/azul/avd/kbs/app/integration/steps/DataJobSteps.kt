package com.azul.avd.kbs.app.integration.steps

import com.azul.avd.kbs.app.processing.addDataName
import io.qameta.allure.Step
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DataJobSteps: JobSteps() {

    @Qualifier("updateDataJob")
    @Autowired
    lateinit var dataJob: Job

    @Step("Run mining job for data: {dataName}")
    fun runDataProcessJob(dataName: String, dbName: String): JobExecution {
        val paramsBuilder = JobParametersBuilder()
        paramsBuilder.addDataName(dataName)
        paramsBuilder.addString("dbName", dbName)
        paramsBuilder.addLocalDateTime("startDate", LocalDateTime.now())
        return jobLauncher.run(dataJob, paramsBuilder.toJobParameters())
    }
}
