package com.mlefree.nuxeoperf.service;

import com.mlefree.nuxeoperf.domain.Scenario;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Scenario.
 */
public interface ScenarioService {

    /**
     * Save a scenario.
     *
     * @param scenario the entity to save
     * @return the persisted entity
     */
    Scenario save(Scenario scenario);

    Scenario launchAndSave(Scenario scenario, JobLauncher jobLauncher);

    /**
     * Get all the scenarios.
     *
     * @return the list of entities
     */
    List<Scenario> findAll();


    /**
     * Get the "id" scenario.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Scenario> findOne(Long id);

    /**
     * Delete the "id" scenario.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
