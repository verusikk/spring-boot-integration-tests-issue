package com.azul.avd.kbs.app.processing.data
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
enum class DataFlowStatuses {
    REQUIRES_UPDATE
}
@Component
@JobScope
class DataUpdateDecider(
    @Value("\${spring.data.mongodb.database}") private val dbName: String
) : JobExecutionDecider {

    override fun decide(jobExecution: JobExecution, stepExecution: StepExecution?): FlowExecutionStatus {
        val paramsDbName = jobExecution.jobParameters.getString("dbName")
        if (paramsDbName != dbName) {
            val message = "Expected db name: $paramsDbName, but got: $dbName"
            error(message)
        }
        Thread.sleep(12_000)
        return FlowExecutionStatus(com.azul.avd.kbs.app.processing.data.DataFlowStatuses.REQUIRES_UPDATE.name)
    }
}
