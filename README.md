# integrations-api
Integrations-api is a REST API built with Java Spring Boot and MySQL as a database. The app have only one entity called 'App Connector'. The application have 6 routes:

`POST /api/connectors` - Add new entity to the DB

`GET /api/connectors` - Get all app connectors. Will return empty array if there aren't any

`GET /api/connectors/{id}` - Get app connector by id. 

`PUT /api/connectors/{id}` - Make Put request on a specific app connector. Will replace the whole entity.

`PATCH /api/connectors/{id}` - Make patch request on a specific app connector. Will update only the specific fields

`DELETE /api/connectors/{id}` - Delete app connector by id

## Pre-requisites
In order to run the project, you need to have docker installed. You can find out how here - https://docs.docker.com/get-docker/


## Getting started
Once you have the above installed, run the following command:
`docker-compose up --build`. This will start two docker containers:
1. MySql database: The database will seed some data to play with. Data will be persisted
2. Integrations-API: Spring Boot REST API.

The application will be served on `http://localhost:8080`

## Testing
The application have unit and integration tests, but if you want to play around with it, there is a postman collection in the repository called `Integrations-api-postman-collection.json`, that can be imported for your convenience.
