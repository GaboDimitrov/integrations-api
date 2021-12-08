package com.integrationsapi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
// This will be AUTO IMPLEMENTED by Spring into a Bean called AppConnectorRepository
// CRUD refers Create, Read, Update, Delete

@Repository
public interface AppConnectorRepository extends CrudRepository<AppConnector, Integer> {

}