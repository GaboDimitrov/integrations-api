package com.integrationsapi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConnectorRepository extends CrudRepository<AppConnector, Integer> {

}