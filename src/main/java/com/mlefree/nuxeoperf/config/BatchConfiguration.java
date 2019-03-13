package com.mlefree.nuxeoperf.config;

import com.mlefree.nuxeoperf.batch.JobCompletionNotificationListener;
import com.mlefree.nuxeoperf.batch.TradeEventProcessor;
import com.mlefree.nuxeoperf.batch.model.TradeEvent;
import com.mlefree.nuxeoperf.batch.model.TradeVolumeStore;
import com.mlefree.nuxeoperf.batch.model.Trade;
import com.mlefree.nuxeoperf.batch.reader.TradeEventReader;
import com.mlefree.nuxeoperf.batch.writer.TradeVolumeAggregator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public TradeVolumeStore tradeVolumeStore() {
        return new TradeVolumeStore();
    }

    @Bean
    public TradeEventReader eventReader() {
        return new TradeEventReader();
    }

    @Bean
    public TradeEventProcessor eventProcessor() {
        return new TradeEventProcessor();
    }

    @Bean
    public TradeVolumeAggregator stockVolumeAggregator() {
        return new TradeVolumeAggregator();
    }

    @Bean
    public JobExecutionListener eventListener() {
        return new JobCompletionNotificationListener();
    }

    @Bean(name="tradeVolumeJob")
    public Job tradeVolumeJob() {
        return jobBuilderFactory
            .get("Trade Volume Job")
            .incrementer(new RunIdIncrementer())
            .listener(eventListener())
            .flow(jobStep())
            .end()
            .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
        asyncTaskExecutor.setConcurrencyLimit(5);
        return asyncTaskExecutor;
    }

    @Bean
    public Step jobStep() {
        return stepBuilderFactory
            .get("Extract -> Transform -> Aggregate -> Load")
            .<TradeEvent, Trade>chunk(1000)
            .reader(eventReader())
            .processor(eventProcessor())
            .writer(stockVolumeAggregator())
            .taskExecutor(taskExecutor())
            .build();
    }

}
