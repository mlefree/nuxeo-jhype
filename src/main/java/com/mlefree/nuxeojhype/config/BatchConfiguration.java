package com.mlefree.nuxeojhype.config;

import com.mlefree.nuxeojhype.batch.JobCompletionNotificationListener;
import com.mlefree.nuxeojhype.batch.TradeEventProcessor;
import com.mlefree.nuxeojhype.batch.model.Trade;
import com.mlefree.nuxeojhype.batch.model.TradeEvent;
import com.mlefree.nuxeojhype.batch.model.TradeVolume;
import com.mlefree.nuxeojhype.batch.model.TradeVolumeStore;
import com.mlefree.nuxeojhype.batch.reader.TradeEventReader;
import com.mlefree.nuxeojhype.batch.writer.TradeVolumeAggregator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

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


    private static final String QUERY_INSERT_TRADE = "INSERT " +
        "INTO NUXEOPERF_TRADE(email_address, name, purchased_package) " +
        "VALUES (:emailAddress, :name, :purchasedPackage)";

    //@Bean
    ItemWriter<TradeVolume> csvFileDatabaseItemWriter(DataSource dataSource,
                                                      NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<TradeVolume> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);

        databaseItemWriter.setSql(QUERY_INSERT_TRADE);

        ItemSqlParameterSourceProvider<TradeVolume> paramProvider =
            new BeanPropertyItemSqlParameterSourceProvider<>();
        databaseItemWriter.setItemSqlParameterSourceProvider(paramProvider);

        return databaseItemWriter;
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
            .<TradeEvent, Trade>chunk(100)
            .reader(eventReader())
            .processor(eventProcessor())
            .writer(stockVolumeAggregator())
            .taskExecutor(taskExecutor())
            .build();
    }

}
