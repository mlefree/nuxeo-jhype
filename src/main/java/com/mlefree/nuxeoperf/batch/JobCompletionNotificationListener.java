package com.mlefree.nuxeoperf.batch;

import com.mlefree.nuxeoperf.batch.model.TradeVolume;
import com.mlefree.nuxeoperf.batch.model.TradeVolumeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private static final String HEADER = "avs,volume";

    private static final String LINE_DILM = ",";

    @Autowired
    private TradeVolumeStore tradeVolumeStore;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.trace("Loading the results into file");
            Path path = Paths.get("avs-volume.gitignored.csv");
            try {
                BufferedWriter fileWriter = Files.newBufferedWriter(path);
                fileWriter.write(HEADER);
                fileWriter.newLine();
                for (TradeVolume pd : tradeVolumeStore.values()) {
                    fileWriter
                        .write(new StringBuilder().append(pd.getVolume())
                        .append(LINE_DILM).append(pd.getVolume()).toString());
                    fileWriter.newLine();
                }
            } catch (Exception e) {
                log.error("Fatal error: error occurred while writing {} file", path.getFileName());
            }
        }
    }
}
