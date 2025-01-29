package com.azul.avd.kbs.app.system.batch.configuration

import org.slf4j.LoggerFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class BatchConfiguration {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean("appThreadPoolScheduler")
    fun appThreadPoolScheduler(


    ): AsyncTaskExecutor {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()
        threadPoolTaskScheduler.setThreadNamePrefix("mtps")
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true)
        threadPoolTaskScheduler.setAwaitTerminationSeconds(120)
        threadPoolTaskScheduler.poolSize = maxOf(1, 4)
        return threadPoolTaskScheduler
    }

    @Bean("asyncJobLauncher")
    fun asyncJobLauncher(
        jobRepository: JobRepository,
        @Qualifier("appThreadPoolScheduler") executor: AsyncTaskExecutor,
    ): JobLauncher {
        val jobLauncher = TaskExecutorJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(executor)
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }
}
