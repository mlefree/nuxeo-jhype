package com.mlefree.nuxeojhype.repository;

import com.mlefree.nuxeojhype.domain.Scenario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Scenario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScenarioRepository extends MongoRepository<Scenario, Long> {

}
