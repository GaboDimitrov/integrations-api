package com.integrationsapi;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = IntegrationTests.Initializer.class)
public class IntegrationTests {

    @ClassRule
    public static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql").withInitScript("setup.sql");

    @Autowired
    private TestRestTemplate restTemplate;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.password=" + mySQLContainer.getPassword(),
                    "spring.datasource.username=" + mySQLContainer.getUsername()
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void shouldGetEntityById() throws Exception {
        ResponseEntity<AppConnector> response = restTemplate.getForEntity("/api/appConnectors/1", AppConnector.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Shopify");
        Assertions.assertThat(response.getBody().getId()).isEqualTo(1);
        Assertions.assertThat(response.getBody().getIsActive()).isEqualTo(true);
    }

    @Test
    public void shouldReturnNotFoundIfEntityWithIdDoesNotExists() throws Exception {
        ResponseEntity<AppConnector> response = restTemplate.getForEntity("/api/appConnectors/1000", AppConnector.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldGetAllEntities() throws Exception {
        ResponseEntity<AppConnector[]> response = restTemplate.getForEntity("/api/appConnectors", AppConnector[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().length).isEqualTo(5);
    }

    @Test
    public void shouldAddNewEntity() throws Exception {
        ResponseEntity<AppConnector> response = restTemplate.postForEntity("/api/add", new AppConnector("Yotpo", "Lorem ipsum"), AppConnector.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Yotpo");
        Assertions.assertThat(response.getBody().getId()).isEqualTo(6);
        Assertions.assertThat(response.getBody().getIsActive()).isEqualTo(false);
    }

    @Test
    public void shouldUpdateWholeEntityWithPutRequest() throws Exception {
        AppConnector newAppConnector =  new AppConnector("Yotpo", "Lorem ipsum");
        restTemplate.put("/api/appConnectors/4", newAppConnector, AppConnector.class);
        ResponseEntity<AppConnector> response = restTemplate.getForEntity("/api/appConnectors/4", AppConnector.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Yotpo");
        Assertions.assertThat(response.getBody().getDescription()).isEqualTo("Lorem ipsum");
        Assertions.assertThat(response.getBody().getId()).isEqualTo(4);
        Assertions.assertThat(response.getBody().getIsActive()).isEqualTo(false);
    }

    @Test
    public void shouldUpdateOnlySentFieldsWithPatchRequest() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("Name", "Adidas");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"name\": \"Adidas\", \"isActive\": false}", headers);

        ResponseEntity<AppConnector> response = restTemplate.exchange("/api/appConnectors/2", HttpMethod.PATCH, entity, AppConnector.class);


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Adidas");
        Assertions.assertThat(response.getBody().getIsActive()).isEqualTo(false);
    }

    @Test
    public void shouldDeleteEntity() throws Exception {
        restTemplate.delete("/api/appConnectors/5");
        ResponseEntity<AppConnector> response = restTemplate.getForEntity("/api/appConnectors/5", AppConnector.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
