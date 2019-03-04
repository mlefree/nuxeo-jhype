package com.mlefree.nuxeoperf.service.impl;

import com.mlefree.nuxeoperf.domain.enumeration.Approach;
import com.mlefree.nuxeoperf.domain.enumeration.ScenarioType;
import com.mlefree.nuxeoperf.service.ScenarioService;
import com.mlefree.nuxeoperf.domain.Scenario;
import com.mlefree.nuxeoperf.repository.ScenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Scenario.
 */
@Service
@Transactional
public class ScenarioServiceImpl implements ScenarioService {

    private final Logger log = LoggerFactory.getLogger(ScenarioServiceImpl.class);

    private final ScenarioRepository scenarioRepository;

    public ScenarioServiceImpl(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;

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

        log.debug("Request to save Scenario : {}", scenario);

        scenario.startDate(Instant.now());
        // todo Something like this ?
        if (scenario.getApproach() == Approach.REST &&
            scenario.getType() == ScenarioType.ImportSmall) {
            new NuxeoServiceImpl().importSmall();
        } else if (scenario.getApproach() == Approach.WEBDRIVER &&
            scenario.getType() == ScenarioType.ImportSmall) {
            new NuxeoServiceImpl().importBulkSmall();
        }
        scenario.endDate(Instant.now());

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
