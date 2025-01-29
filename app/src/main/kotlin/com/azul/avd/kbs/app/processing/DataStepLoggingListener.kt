package com.azul.avd.kbs.app.processing

import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class DataStepLoggingListener : StepExecutionListener {
    private val logger = LoggerFactory.getLogger(DataStepLoggingListener::class.java)

    override fun beforeStep(stepExecution: StepExecution) {
        val dataName = stepExecution.jobParameters.getDataName()
        logger.info("Executing step [${stepExecution.stepName}] for data: $dataName")
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        val dataName = stepExecution.jobExecution.jobParameters.getDataName()

        val duration = stepExecution.endTime?.let {
            Duration.between(
                stepExecution.startTime,
                stepExecution.endTime
            )
        }

        logger.info(
            "Step [${stepExecution.stepName}] executed in: $duration, " +
                    "for data: $dataName with status: ${stepExecution.status}"
        )
        return stepExecution.exitStatus
    }
}
