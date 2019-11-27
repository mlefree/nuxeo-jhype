package com.mlefree.nuxeojhype.service.impl;

import com.mlefree.nuxeojhype.domain.Scenario;
import com.mlefree.nuxeojhype.domain.enumeration.Approach;
import com.mlefree.nuxeojhype.domain.enumeration.ScenarioType;
import com.mlefree.nuxeojhype.repository.ScenarioRepository;
import com.mlefree.nuxeojhype.service.NuxeoService;
import com.mlefree.nuxeojhype.service.ScenarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing Scenario.
 */
@Service
//@Transactional
public class ScenarioServiceImpl implements ScenarioService {

    private final Logger log = LoggerFactory.getLogger(ScenarioServiceImpl.class);

    private final ScenarioRepository scenarioRepository;

    private final NuxeoService nuxeoService;

    @Autowired
    Job tradeVolumeJob;

    public ScenarioServiceImpl(ScenarioRepository scenarioRepository, NuxeoService nuxeoService) {
        this.scenarioRepository = scenarioRepository;
        this.nuxeoService = nuxeoService;
    }

    private JobParameters createInitialJobParameterMap() {
        Map<String, JobParameter> m = new HashMap<>();
        m.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters p = new JobParameters(m);
        return p;

        // JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        //    	jobParametersBuilder.addString("INPUT_FILE_PATH", inputFileName);
        //    	jobParametersBuilder.addLong("TIMESTAMP",new Date().getTime());
        //
        //    	jobParametersBuilder.toJobParameters();
    }


    /**
     * Save a scenario.
     *
     * @param scenario the entity to save
     * @return the persisted entity
     */
    @Override
    public Scenario save(Scenario scenario) {
        log.debug("Request to save Scenario : {}", scenario);
        return scenarioRepository.save(scenario);
    }


    /**
     * Save a scenario.
     *
     * @param scenario the entity to save
     * @return the persisted entity
     */
    @Override
    public Scenario launchAndSave(Scenario scenario, JobLauncher jobLauncher) {
        log.debug("Request to save Scenario : {}", scenario);


        try {
            scenario.startDate(Instant.now());
            // todo map all scenario

            if (scenario.getApproach() == Approach.REST &&
                scenario.getType() == ScenarioType.ImportSmall) {
                nuxeoService.importSmall();
            } else if (scenario.getApproach() == Approach.WEBDRIVER &&
                scenario.getType() == ScenarioType.ImportSmall) {
                nuxeoService.importBulkSmall();
            } else if (scenario.getApproach() == Approach.REST &&
                scenario.getType() == ScenarioType.ImportBig) {


                jobLauncher.run(tradeVolumeJob, createInitialJobParameterMap());
                //nuxeoService.importBulkSmall();
            }



            scenario.endDate(Instant.now());

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }



        return scenarioRepository.save(scenario);
    }


    /**
     * Get all the scenarios.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Scenario> findAll() {
        log.debug("Request to get all Scenarios");
        return scenarioRepository.findAll();
    }


    /**
     * Get one scenario by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Scenario> findOne(Long id) {
        log.debug("Request to get Scenario : {}", id);
        return scenarioRepository.findById(id);
    }

    /**
     * Delete the scenario by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scenario : {}", id);        scenarioRepository.deleteById(id);
    }
}
