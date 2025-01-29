package com.azul.avd.kbs.app.processing.data

import com.azul.avd.kbs.app.processing.getDataName
import com.azul.avd.kbs.app.system.batch.QueueItemReader
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

const val DATA_JOB = "DataJob"

@Configuration
class DataBatchJob(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
) {
    @Bean("updateDataJob")
    fun dataJob(
        @Qualifier("collectData") collectModules: Step,
        dataUpdateDecider: DataUpdateDecider,
    ): Job {
        return JobBuilder(DATA_JOB, jobRepository)
            .start(dataUpdateDecider)
            .on(DataFlowStatuses.REQUIRES_UPDATE.name)
            .to(collectModules)
            .end()
            .build()
    }

    @Bean("collectData")
    @JobScope
    fun collectData(
        @Qualifier("dataReader") reader: ItemReader<String?>,
        @Qualifier("dataWriter") writer: ItemWriter<String>,
        @Value("#{jobParameters}") jobParameters: Map<String, Any>,
    ): Step {
        return StepBuilder("CollectData", jobRepository)
            .chunk<String, String>(1, transactionManager)
            .reader(reader)
            .processor { item ->
                println(item)
                item
            }
            .writer(writer)
            .faultTolerant()
            .build()
    }

    @Bean("dataReader")
    @StepScope
    fun softwareReader(
        @Value("#{jobParameters}") jobParameters: Map<String, Any>,
    ): QueueItemReader<String?> {
        val dataName = jobParameters.getDataName()
        return QueueItemReader(listOf(dataName))
    }

    @Bean("dataWriter")
    fun softwareWriter(): ItemWriter<String> {
        return ItemWriter<String> { chunk ->
            for (dataName in chunk) {
                println("do nothing for: ${dataName}")
            }
        }
    }
}
