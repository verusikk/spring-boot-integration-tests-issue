package com.azul.avd.kbs.app.integration.steps

import com.azul.avd.kbs.app.integration.testutils.toJsonString
import io.qameta.allure.Allure
import io.qameta.allure.Allure.step
import io.qameta.allure.Step
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Duration

@Service
open class JobSteps {

    private val defaultFinishedStatuses = listOf(BatchStatus.COMPLETED, BatchStatus.FAILED, BatchStatus.STOPPED)

    @Autowired
    lateinit var jobExplorer: JobExplorer

    @Qualifier("asyncJobLauncher")
    @Autowired
    lateinit var jobLauncher: JobLauncher

    @Step("Wait job finished and check state=COMPLETED")
    fun waitJobCompleted(execution: JobExecution) {
        val job = waitJobFinished(execution)
        checkExecutionStatus(job, listOf(BatchStatus.COMPLETED))
    }

    @Step("Wait job finish")
    fun waitJobFinished(execution: JobExecution, timeout: Duration = Duration.ofMinutes(10)): JobExecution {
        return waitJobStatus(execution, defaultFinishedStatuses, timeout)
    }

    @Step("Wait job status")
    fun waitJobStatus(execution: JobExecution, waitForStatuses: List<BatchStatus>,
                      timeout: Duration = Duration.ofMinutes(10)): JobExecution {
        var jobExecution: JobExecution? = jobExplorer.getJobExecution(execution.id)
        step("Wait for job status: ${waitForStatuses.joinToString(", ")}") { _ ->
            val deadline = System.currentTimeMillis() + timeout.toMillis()
            Awaitility.with().pollInterval(Duration.ofMillis(500)).await()
                .atMost(timeout.plus(Duration.ofSeconds(3)))
                .until {
                    jobExecution = jobExplorer.getJobExecution(execution.id)
                    waitForStatuses.contains(jobExecution?.status) || System.currentTimeMillis() >= deadline
                }
            checkExecutionStatus(jobExecution!!, waitForStatuses)
        }
        return jobExecution!!
    }

    private fun checkExecutionStatus(execution: JobExecution, statuses: List<BatchStatus>) {
        try {
            println(execution.failureExceptions.toJsonString())
            assertThat(execution.status)
                .`as`("Execution: ${execution}, steps: ${execution.stepExecutions})")
                .isIn(statuses)
        } catch (th: Throwable) {
            attachJobAndSteps(execution)
            throw th
        }
    }

    fun attachJobAndSteps(execution: JobExecution) {
        Allure.addAttachment("Execution", execution.toString())
        Allure.addAttachment("Steps", execution.stepExecutions.toString())
    }
}
